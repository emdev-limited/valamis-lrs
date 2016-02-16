package com.arcusys.valamis.lrs.jdbc.database.api

import java.net.URI
import java.util.UUID

import com.arcusys.valamis.lrs.exception.ConflictEntityException
import com.arcusys.valamis.lrs.jdbc.database.converter._
import com.arcusys.valamis.lrs.jdbc.database.api.query.TypeAliases
import com.arcusys.valamis.lrs.jdbc.database.row._
import com.arcusys.valamis.lrs.jdbc.database.LrsDataContext
import com.arcusys.valamis.lrs.jdbc.database.api.query.{StatementQueries, TypeAliases}
import com.arcusys.valamis.lrs.jdbc.database.row.StatementRow
import com.arcusys.valamis.lrs.jdbc.Loggable
import com.arcusys.valamis.lrs.tincan._
import com.arcusys.valamis.lrs._
import org.joda.time.DateTime

import scala.async.Async
import scala.concurrent._
import scala.async.Async.async
import scala.slick.jdbc.JdbcBackend

/**
 * Created by Iliya Tryapitsin on 08.07.15.
 */
trait StatementApi extends StatementQueries {
  this: LrsDataContext with Loggable
  with ResultApi
  with ScoreApi
  with StatementObjectApi
  with AttachmentApi
  with AccountApi
  with ContextApi
  with SubStatementApi
  with StatementRefApi
  with ActorApi
  with ActivityApi
  with TypeAliases =>

  import executionContext.driver.simple._
  import jodaSupport._

  /**
   * Find Tincan [[Statement]] by [[Statement.id]] exclude voided statements
   * @param id Tincan [[Statement.id]]
   * @param s
   * @return Tincan [[Statement]] or None
   */
  def findStatementById(id: String)
                       (implicit s: Session): Future[Option[Statement]] =
    if (isVoided (id)) async { None }
    else findVoidedStatement(id)

  /**
   * Find Tincan [[Statement]] by [[Statement.id]] include voided [[Statement]]s
   * @param id Tincan [[Statement.id]]
   * @param s
   * @return Tincan [[Statement]] or None
   */
  def findVoidedStatement (id: String)
                          (implicit s: Session): Future[Option[Statement]] = async {

    val resultTask      = async { findResultByStatementIdQC (id).firstOption whenDefined convertResult  }
    val contextTask     = async { findContextByStatementIdQC(id).firstOption whenDefined convertContext }
    val attachmentsTask = async { findAttachmentsByStatementIdQC(id).run map { it => it convert }       }
    val statementTask   = async { findStatementByIdQC (id).firstOption } flatMap {
      case Some(statementRec) =>
        val actorTask           = async { findActorByKey (statementRec.actorKey)               }
        val authorityTask       = async { statementRec.authorityKey whenDefined findAgentByKey }
        val statementObjectTask = async { findStatementObjectByKey (statementRec.objectKey)    }

        for {
          actor           <- actorTask
          result          <- resultTask
          context         <- contextTask
          authority       <- authorityTask
          attachments     <- attachmentsTask
          statementObject <- statementObjectTask
        } yield {
          val statement = statementRec.convert withActor {
            actor

          } withResult {
            result

          } withContext {
            context

          }  withAuthority {
            authority

          } withAttachments {
            attachments

          } withObj {
            statementObject

          } build

          statement ?
        }

      case None => async { None }
    }

    Async await statementTask
  }

  def findStatementsByParams (q: StatementQuery)
                             (implicit s: Session): Future[PartialSeq[Statement]] = async {
    val query = statements.withoutVoided filterRegistration {
      q registration

    } filterVerb {
      q verb

    } filterActivities (
      q.activity, q.relatedActivities

    ) filterActor (
      q.agent, q.relatedAgents
    ) onlyWithAttachments q.attachments sortByStore q.ascending since q.since until q.until

    val recsQuery = query limit {
      q.limit + q.offset
    } drop q.offset

    val countQueryTask  = async { recsQuery.length run      }
    val countTask       = async { statements.length run }

    val result = for {
      c <- countQueryTask
      l <- countTask
      s <- buildStatements (recsQuery run)
    } yield s toPartialSeq (l == c)

    Async await result
  }

  /**
   * Building Tincan [[Statement]]s by storage records
   * @param recs List of [[StatementRow]]
   * @param s
   * @return List of Tincan [[Statement]]s
   */
  private def buildStatements (recs: Seq[StatementRow])
                              (implicit s: Session): Future[Seq[Statement]] =
    if (recs isEmpty) async { Seq() }
    else {
      val authorityKeys    = recs filter { it => it.authorityKey isDefined } map { _.authorityKey get }
      val statementKeys    = recs map { _ key       }
      val actorKeys        = recs map { _ actorKey  }
      val statementObjKeys = recs map { _ objectKey }

      // start async queries to data storage
      val statementObjectsTask = findStatementObjectsByKeysAsync (
        authorityKeys ++ actorKeys ++ statementObjKeys
      )

      val resultTask = async {
        findResultsByStatementKeysQ (statementKeys).run map { _._2 } then convertResults
      }

      val contextTask = async {
        findContextsByStatementKeysQ (statementKeys).run map { _._2 } then convertContexts
      }

      val attachmentsTask = async {
        findAttachmentsByStatementKeysQ (statementKeys).run map { x => (x.statementId, x convert) }
      }

      // a wait async tasks result
      for {
        results           <- resultTask
        contexts          <- contextTask
        attachments       <- attachmentsTask
        statementObjects  <- statementObjectsTask
      } yield recs map {

        // building the Tincan Statement instances
        it =>
          val actor = statementObjects get it.actorKey whenDefined {
            x => x.asInstanceOf[Actor]
          } get

          val authority = it.authorityKey whenDefined {
            statementObjects.get(_).get.asInstanceOf[Actor]
          }

          val result  = it.resultKey  whenDefined { r => results .get(it.resultKey ) } flatMap { x => x }
          val context = it.contextKey whenDefined { r => contexts.get(it.contextKey) } flatMap { x => x }
          val attachment = attachments filter { _._1 == it.key } map { _._2 }

          it.convert withActor {
            actor.asInstanceOf[Actor]

          } withAuthority {
            authority

          } withObj {
            statementObjects get it.objectKey get

          } withResult {
            result

          } withContext {
            context

          } withAttachments {
            attachment

          } build
      }
  }

  /**
   * Save new [[Statement]] in the LRS
   * @param statement [[Statement]] instance
   * @return Saved [[Statement.id]]
   */
  def addStatement(statement: Statement): UUID = db.withTransaction { implicit session =>

    if (statement exists)
      throw new ConflictEntityException(s"Statement with key = '${statement.id}' already exist")

    statements add statement
  } then UUID.fromString

  /**
   * Return [[Statement]]s since date. If date is None return latest TakeCount [[Statement]]s
   * @param date Start date
   * @return List of [[Statement]]s
   */
  def statementsSince (date: Option[DateTime]) =
    date whenDefined { findStatementsSinceDateQC(_) } getOrElse { takeTopStatementsQC(TakeCount) }

  /**
   * Return [[Statement]]s since date. If date is None return latest TakeCount [[Statement]]s
   * @param date Start date
   * @param s
   * @return List of [[Statement]]s
   */
  def statementsCountByFromAndVerb (date: Option[DateTime], verb: Option[URI]) =
    date whenDefined {
      d => statements filter { it => it.timestamp >= d }
    } getOrElse {
      statements

    } then { q =>
      verb whenDefined {
        x => q filter { it => it.verbId like x.toString }
      } getOrElse q

    } length


  def isVoided (id: String)
               (implicit s: Session) = findStatementRefByIdQC (id).firstOption isDefined

  implicit class StatementInsertQueries (q: StatementQ) {

    /**
     * Insert Tincan [[Statement]] to the storage
     * @param s [[Statement]] instance
     * @param session
     * @return Identity key in the storage
     */
    def add (s: Statement)
            (implicit session: Session): StatementRow#Type =
      s.convert withObj {
        statementObjects addExt s.obj

      } withResult {
        results add s.result

      } withContext {
        contexts add s.context

      } withAuthority {
        actors add s.authority

      } withActor {
        actors addUnique s.actor

      } build { r =>
        q += r

      } then { key =>
        s.attachments map { a =>
          a.convert withStatement key build

        } then { a =>
          attachments addSeq a
        }
        key
      }
  }

  implicit class StorageStatementExtension (s: Statement)  {

    /**
     * Check are exist Tincan [[Statement]] in a storage
     * @param session
     * @return Key in storage if exist
     */
    def exists(implicit session: Session): Boolean = s.id match {
      case Some(v) => findStatementByIdQC (v toString).firstOption isDefined
      case None => false
    }
  }

  implicit class StatementApiExtension(q: StatementQ) {

    /**
     * Filtering [[Statement]]s by [[Statement.timestamp]] from start date and time
     * @param since Start date and time
     * @param s
     * @return [[Statement]] storage query
     */
    def since (since: Option[DateTime])
              (implicit s: Session): StatementQ =
      since map {
        dt => q filter { _.timestamp >= dt }
      } getOrElse q

    /**
     * Filtering [[Statement]]s by [[Statement.timestamp]] to end date and time
     * @param until End date and time
     * @param s
     * @return [[Statement]] storage query
     */
    def until (until: Option[DateTime])
              (implicit s: Session): StatementQ =
      until map {
        dt => q filter { _.timestamp <= dt }
      } getOrElse q

    /**
     * Return [[Statement]]s by [[Statement.timestamp]] from start date and time
     * @param limit Start date and time
     * @param s
     * @return [[Statement]] storage query
     */
    def limit (limit: Int)
              (implicit s: Session): StatementQ =
      if (limit != 0) q take limit
      else q

    /**
     * Filtering exclude voided statements
     * @param s
     * @return [[Statement]] storage query
     */
    def withoutVoided (implicit s: Session): StatementQ =
      q filterNot { x => isVoided(x) }

    def isVoided (t: StatementsTable)
                 (implicit s: Session) =
      t.key in {
        statementReferences join q on { (x1, x2) =>
          x1.key === x2.objectKey

        } filter {
          case (x1, x2) => x2.verbId like Constants.Tincan.VoidedVerb

        } map { case (x1, x2) => x1.statementId }
      }

    /**
     * Order result by a store time
     * @param asc
     * @param s
     * @return  [[Statement]] storage query
     */
    def sortByStore (asc: Boolean)
                    (implicit s: Session): StatementQ =
      q sortBy { x => if (asc) x.stored.asc else x.stored.desc }

    /**
     * Filtering Tincan [[Statement]]s by Tincan [[Actor]]
     * @param arg Tincan [[Actor]] and relative
     * @param s
     * @return [[Statement]] storage query
     */
    def filterActor (arg: (Option[Actor], Boolean)): StatementQ  =
      arg._1 map { actor =>
        val subStatementQuery = subStatements keyForQ actor
        val actorKeyQuery     = actors        keyForQ actor
        val contextQuery      = contexts      keyForQ actor

        if (arg._2) q filter { it =>
          it.actorKey in actorKeyQuery

        } else q filter { it =>
          (it.actorKey   in actorKeyQuery     ) ||
          (it.objectKey  in subStatementQuery ) ||
          (it.contextKey in contextQuery      )
        }
      } getOrElse q

    def filterActivities (arg: (Option[URI], Boolean))
                         (implicit session: JdbcBackend#Session): StatementQ =
      arg._1 map { activityId =>
        val activityKeyQuery = activities filter {
          a => a.id === activityId.toString
        } map { _.key }

        val subStatementKeyQuery = subStatements filter {
          s => s.statementObjectKey in activityKeyQuery
        } map { _.key }

        val contextKeyQuery = contextActivities filter {
          c => c.activityKey in activityKeyQuery
        } map { _.contextKey }

        if (arg._2) q filter { st =>
          (st.objectKey  in activityKeyQuery    ) ||
          (st.objectKey  in subStatementKeyQuery) ||
          (st.contextKey in contextKeyQuery     )

        } else q filter { st =>
          st.objectKey in activityKeyQuery
        }
      } getOrElse q

    def filterActivities (arg: (Seq[URI], Boolean)): StatementQ = {

      val activityKeys = arg._1 map { _.toString }

      val activityKeyQuery = activities filter {
        a => a.id inSetBind activityKeys
      } map { _.key }

      val subStatementKeyQuery = subStatements filter {
        s => s.statementObjectKey in activityKeyQuery
      } map { _.key }

      val contextKeyQuery = contextActivities filter {
        c => c.activityKey in activityKeyQuery
      } map { _.contextKey }

      if (arg._2) q filter { st =>
        (st.objectKey  in activityKeyQuery    ) ||
        (st.objectKey  in subStatementKeyQuery) ||
        (st.contextKey in contextKeyQuery     )

      } else q filter { st =>
        st.objectKey in activityKeyQuery
      }

    }

    def filterVerb (uri: Option[URI])
                   (implicit s: Session): StatementQ =
      uri map { u =>
        q filter { x => x.verbId === u.toString }
      } getOrElse q

    def filterVerbs (uries: Seq[URI]) = {
      val u = uries map { _.toString }
      q filter { x => x.verbId inSetBind u }
    }

    def filterRegistration (reg: Option[UUID])
                           (implicit s: Session): StatementQ =
      reg map { r =>
        q filter { x => x.contextKey === r.toString }
      } getOrElse q

    def onlyWithAttachments (useAttachments: Boolean)
                            (implicit session: Session): StatementQ =
      if (useAttachments)
        q innerJoin attachments on {
          (x1, x2) => x1.key === x2.statementKey

        } map { _._1 }
      else q
  }
}
