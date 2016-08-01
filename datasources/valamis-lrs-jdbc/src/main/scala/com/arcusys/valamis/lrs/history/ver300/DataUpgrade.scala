package com.arcusys.valamis.lrs.history.ver300


import com.arcusys.valamis.lrs.history.BaseUpgrade
import com.arcusys.valamis.lrs.jdbc.JdbcLrs
import com.arcusys.valamis.lrs.jdbc.database.row.{ContextActivityContextRow, ContextActivityActivityRow}


import scala.annotation.tailrec
import scala.slick.driver.JdbcDriver
import scala.slick.jdbc.JdbcBackend


class DataUpgrade(val lrs: JdbcLrs,
                  val jdbcDriver: JdbcDriver,
                  val database: JdbcBackend#Database) extends BaseUpgrade {

  import jdbcDriver.simple._

  def upgrade = {
    migrateActivities
    migrateContext
  }


  private def migrateActivities = {
    logger.info("Migrate activities")

    val activitiesQuery = lrs.contextActivities
      .map { a => (a.activityKey, a.contextActivityType) }
      .groupBy(identity)
      .map(_._1)

    insertInContextActivityActivity(0, 200000)

    @tailrec
    def insertInContextActivityActivity(begin: Int, count: Int): Unit = {

      val inserted = lrs.db.withSession { implicit session =>

        val newActivities = activitiesQuery.drop(begin).take(count).list
          .map { a => ContextActivityActivityRow(activityKey = a._1, tpe = a._2) }

        newActivities match {
          case Nil => 0
          case activityInsert =>
            lrs.contextActivitiesActivity ++= activityInsert
            activityInsert.size
        }
      }

      if (inserted > 0) {
        val from = begin + count
        insertInContextActivityActivity(from, count)
      }
    }
  }


  private def migrateContext = {
    logger.info("Migrate activities")

    val contextsQuery = (lrs.contextActivities leftJoin lrs.contextActivitiesActivity on { (ca, caa) =>
      ca.activityKey === caa.activityKey
    }).map { x => (x._1.contextKey, x._2.key) }
      .groupBy(identity)
      .map(_._1)

    insertInContextActivityContext(0, 200000)

    @tailrec
    def insertInContextActivityContext(begin: Int, count: Int): Unit = {
      val inserted = lrs.db.withSession { implicit session =>

        val newContext = contextsQuery.drop(begin).take(count).list
          .map { c => ContextActivityContextRow(contextKey = c._1, activityKey = c._2) }

        newContext match {
          case Nil => 0
          case contextInsert =>
            lrs.contextActivitiesContext ++= contextInsert
            contextInsert.size
        }
      }

      if (inserted > 0) {
        val from = begin + count
        insertInContextActivityContext(from, count)
      }
    }
  }
}
