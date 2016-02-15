package com.arcusys.valamis.lrs.jdbc

import java.net.URI

import com.arcusys.valamis.lrs._
import com.arcusys.valamis.lrs.SeqWithCount
import com.arcusys.valamis.lrs.jdbc.database.LrsDataContext
import com.arcusys.valamis.lrs.jdbc.database.api._
import com.arcusys.valamis.lrs.jdbc.database.api.query.TypeAliases
import com.arcusys.valamis.lrs.jdbc.database.utils.CustomOperator
import com.arcusys.valamis.lrs.tincan._
import com.arcusys.valamis.lrs.tincan.valamis.ActivityIdLanguageMap
import org.joda.time.DateTime
import com.arcusys.valamis.lrs.jdbc.database.utils._

import scala.util.Try

/**
 * Created by Iliya Tryapitsin on 21.07.15.
 */
trait ValamisReporter
  extends LrsDataContext
  with ActivityProfileApi
  with AttachmentApi
  with TypeAliases
  with StatementApi
  with AgentProfileApi
  with DocumentApi
  with ResultApi
  with ScoreApi
  with StatementObjectApi
  with AccountApi
  with ContextApi
  with SubStatementApi
  with StatementRefApi
  with ActorApi
  with ActivityApi
  with Loggable
  with CustomColumnsExtensions{

  val executionContext: ExecutionContext

  import executionContext.driver.simple._
  import jodaSupport._

  /**
   * Get Tincan Verb amount since date time
   * @param start Start date time
   * @param verb Verb URI
   * @return Count of found verbs
   */
  def verbAmount(start: Option[DateTime] = None,
                 verb:  Option[URI] = None): Int = executionContext run {
    start whenDefined {
      d => statements filter { it => it.timestamp >= d }
    } getOrElse {
      statements

    } then { q =>
      verb whenDefined {
        x => q filter { it => it.verbId like x.toString }
      } getOrElse q

    } length
  }

  /**
   * Get Tincan [[Verb]] amount since date time grouped by Tincan [[Verb.id]]
   * @param start Start date time
   * @return List of [[Verb.id]] and [[Verb]] count
   */
  def verbAmountByGroup(start: Option[DateTime]): Seq[(String, Int)] = {
    executionContext run statementsSince(start) groupBy { s =>
      s.verbId
    } map { case (id, s) => (id, s.length) } toSeq
  }

  def verbIdsWithDate(start: Option[DateTime]): Seq[(String, DateTime)] =
    executionContext run statementsSince(start) map { s => (s.verbId, s.timestamp) }

  /**
   * Find Tincan [[Verb]] with [[Activity.id]] and [[Activity.description]]
   * @param filter Filter for [[Verb.display]] and [[Activity.name]]
   * @param limit Maximum count
   * @param offset Offset from head
   * @param sortDir Sort direction
   * @return List Tincan [[Verb]] with [[Activity.id]] and [[Activity.description]]
   */
  def verbWithActivities(filter:  Option[String] = None,
                         limit:   Int            = 10,
                         offset:  Int            = 0,
                         sortNameDesc: Boolean   = true,
                         sortTimeDesc: Boolean   = false,
                         sortTimeFirst: Boolean  = false): SeqWithCount[(Verb, ActivityIdLanguageMap, Option[DateTime])] = {

      val query = statements join activities on {
        (s, o) => s.objectKey === o.key

      } filter  {
        case ((s, o)) => (s.verbDisplay.lower like filter) || (o.name.lower like filter)
      } map {
        case ((s, o)) => (s.verbId, s.verbDisplay, o.id, o.name, s.timestamp)

      } groupBy(g => (g._1, g._2, g._3, g._4)) map {
        case (k, v) => (k, v.map(_._5).max)

      } sortBy {
        case ((verbId, _, _, _), timestamp) =>
          val sortVerbId = if (sortNameDesc) verbId.asc else verbId.desc
          var sortTime = if (sortTimeDesc) timestamp.asc else timestamp.desc
          if (sortTimeFirst) (sortTime, sortVerbId) else (sortVerbId, sortTime)
      }

      val count  = executionContext run query.length
      val result = executionContext from { query drop offset take limit } select

      SeqWithCount(
        result map { rec => (Verb(rec._1._1, rec._1._2), ActivityIdLanguageMap(rec._1._3, rec._1._4), rec._2) },
        count
      )
    }

  def findMaxActivityScaled (actor:  Actor,
                             verbId: Verb#Id) = executionContext run {
    val actorKeyQ = actors keyForQ actor

    statements join activities on {
      (x1, x2) => x1.objectKey === x2.key
    } filter {
      case (x1, x2) => x1.verbId === verbId

    } filter {
      case (x1, x2) => x1.actorKey in actorKeyQ

    } join results on {
      case ((x1, x2), x3) => x1.resultKey === x3.key

    } join scores.filter { x => x.scaled.isDefined } on {
      case (((x1, x2), x3), x4) => x3.scoreKey === x4.key

    } map {
      case (((x1, x2), x3), x4) => (x2.id, x4)

    } groupBy { case (x1, x2) => x1 } map {
      case (k, values) => (k, values.map(x => x._2.scaled).max)
    }
  }

  def findStatementsCount (agent: Agent,
                           verbs: Seq[URI]): Int = executionContext run {
    statements filterActor (agent ?, true) filterVerbs verbs length
  }

  def findMinDate (agent:       Agent,
                   verbs:       Seq[URI],
                   activityIds: Seq[URI],
                   since:       DateTime): Seq[(URI, Activity#Id, DateTime)] = {
    val query = statements filterActor (agent ?, true) filterVerbs verbs filterActivities (activityIds, true) filter {
      x => x.stored >= since

    } join activities on {
      (x1, x2) => x1.objectKey === x2.key

    } map {
      case (x1, x2) => (x1.verbId, x2.id, x1.stored)

    } groupBy { x => (x._1, x._2) } map { case ((verbId, id), dtq) =>
      val minStoredTimeQuery = dtq map { x => x._3 } min

      (verbId, id, minStoredTimeQuery)
    }

    executionContext from query select

  } map { case (uri, id, dt) => (new URI(uri), id, dt get) }
}
