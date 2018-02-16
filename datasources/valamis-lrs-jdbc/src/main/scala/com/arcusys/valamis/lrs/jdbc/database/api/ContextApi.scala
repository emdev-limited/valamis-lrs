package com.arcusys.valamis.lrs.jdbc.database.api

import com.arcusys.valamis.lrs._
import com.arcusys.valamis.lrs.utils._
import com.arcusys.valamis.lrs.jdbc.database.converter._
import com.arcusys.valamis.lrs.jdbc.database.api.query._
import com.arcusys.valamis.lrs.jdbc.database.row._
import com.arcusys.valamis.lrs.jdbc.database.LrsDataContext
import com.arcusys.valamis.lrs.tincan._

/**
 * Created by Iliya Tryapitsin on 08.07.15.
 */
trait ContextApi extends ContextActivityQueries {
  this: LrsDataContext
    with ActorApi
    with AccountApi
    with ActivityApi
    with StatementRefApi =>

  import driver.simple._

  protected def convertContext (rec: ContextRow)
                               (implicit s: Session) =
    rec.convert withInstructor {
      rec.getInstructor

    } withTeam {
      rec.getTeam

    } withStatement {
      rec.getStatementRef

    } withContextActivities {
      rec.getContextActivities

    } build

  protected def convertContexts (recs: Seq[ContextRow])
                                (implicit s: Session): Map[ContextRow#KeyType, Context] = {
    val instructors = findActorsByKeys (
      recs map { r => r instructor } takeDefined ()
    )

    val teams = findGroupsByKeys (
      recs map { r => r.team } takeDefined()
    )

    val statementRefs = findStatementRefsByKeys(
      recs map { r => r.statement } takeDefined()
    )

    val contextActivities = findContextActivitiesByContextKeys(
      recs map { r => r.key get}
    )

    recs map { rec =>
      val cntx = rec.convert withInstructor {
       rec.instructor map { instructors get _  get }

      } withTeam {
        rec.team map { teams get _  get }

      } withStatement {
        rec.statement map { statementRefs get _ get }

      } withContextActivities {
        contextActivities get rec.key.get flatMap { x=>x }
      } build

      (rec.key, cntx)
    } toMap
  }

  def findContextActivitiesByContextKeys (keys: Seq[ContextRow#Type])
                                         (implicit s: Session): Map[ContextRow#Type, Option[ContextActivities]]= {
    findContextActivitiesByContextKeysQ (keys).run afterThat { records =>
      records groupBy { case (x1, x2) => x1._1.contextKey } map {
        case (k, v) => (k, contextActivitiesRowsToTincan (v))
      }
    }
  }

  private def contextActivitiesRowsToTincan (seq: Seq[((ContextActivityContextRow, ContextActivityActivityRow), ActivityRow)]) =
    if (seq.length > 0) {
      val parents = seq filter {
        case (x1, x2) => x1._2.tpe == ContextActivityType.Parent
      } map {
        case (x1, x2) => ActivityReference(id = x2.id)
      }

      val grouping = seq filter {
        case (x1, x2) => x1._2.tpe == ContextActivityType.Grouping
      } map {
        case (x1, x2) => ActivityReference(id = x2.id)
      }

      val category = seq filter {
        case (x1, x2) => x1._2.tpe == ContextActivityType.Category
      } map {
        case (x1, x2) => ActivityReference(id = x2.id)
      }

      val other = seq filter {
        case (x1, x2) => x1._2.tpe == ContextActivityType.Other
      } map {
        case (x1, x2) => ActivityReference(id = x2.id)
      }

      ContextActivities(
        parent = parents,
        grouping = grouping,
        category = category,
        other = other
      ) ?

    } else None

  implicit class ContextRowExtension (rec: ContextRow) {
    def getInstructor(implicit s: Session): Option[Actor] =
      rec.instructor map { findActorByKey }

    def getTeam(implicit s: Session): Option[Group] =
      rec.team map { findGroupByKey }

    def getStatementRef(implicit s: Session): Option[StatementReference] =
      rec.statement map { findStatementRefByKey }

    def getContextActivities(implicit s: Session): Option[ContextActivities] =
      findContextActivitiesByContextKeyQC (rec.key get).run afterThat contextActivitiesRowsToTincan
  }

  implicit class ContextInsertQueries (q: ContextQ) {

    /**
     * Insert Tincan Context to the storage
     * @param context Context instance
     * @param session
     * @return Identity key in the storage
     */
    def add (context: Option[Context])
            (implicit session: Session): ContextRow#KeyType =
      context map { x => q add x }

    /**
     * Insert Tincan Context to the storage
     * @param context Context instance
     * @param session
     * @return Identity key in the storage
     */
    def add (context: Context)
            (implicit session: Session): ContextRow#Type =
      context.convert withInstructor {
        actors add context.instructor

      } withRef {
        statementReferences addUnique context.statement

      } withTeam {
        actors add context.team

      } build { r =>
        q += r

      } afterThat { key =>
        context.contextActivities map { x =>

          val withKeys = activities keysFor x.allIds

          activities addSeq {
            withKeys filter { y => y._2.isEmpty } map { y =>
              Activity(id = y._1)
            }
          } map { x =>
            (x._1, x._2 ?)

          } afterThat { result =>
            result ++ withKeys filter { y => y._2.isDefined }

          } afterThat { k =>
            val data = x.convert withContext key build k
            data.map { d =>
              val contextActivity = ContextActivityActivityRow(activityKey = d._2, tpe = d._1)

              val result = contextActivitiesActivity.filter(a => (a.activityKey === d._2 && a.contextActivityType === d._1)).map(_.key).run

              val keyActivity = result.headOption.getOrElse(
                contextActivitiesActivity.returning(contextActivitiesActivity.map(_.key)).insert(contextActivity))
                contextActivitiesContext.insert(ContextActivityContextRow(contextKey = d._3, activityKey = keyActivity))

            }
          }
        }
        key
      }

    def keyForQ (actor: Actor) =
      q filter { it =>
        val actorKeyQuery = actors keyForQ actor
        (it.instructor in actorKeyQuery) ||
          (it.team in actorKeyQuery)
      } map {
        x => x.key
      }
  }
}
