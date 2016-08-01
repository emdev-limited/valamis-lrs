package com.arcusys.valamis.lrs.history.ver230

import com.arcusys.valamis.lrs.history.BaseUpgrade
import com.arcusys.valamis.lrs.history.ver230.from.DataContext
import com.arcusys.valamis.lrs.jdbc.JdbcLrs

import scala.slick.jdbc.JdbcBackend
import scala.util._
/**
 * Created by iliyatryapitsin on 16.04.15.
 */
class DataUpgrade(val lrs: JdbcLrs) extends BaseUpgrade{
  val dataContext = new DataContext

  def upgrade = lrs.db.withSession { implicit session =>
    tryAction { migrateStatements       }
    tryAction { migrateAgentProfiles    }
    tryAction { migrateActivityProfiles }
    tryAction { migrateActivityStates   }
  }

  private def migrateStatements(implicit session: JdbcBackend#Session) = {
    logger.info("Migrate statements")
    val statements = dataContext.getStatements
    val successStatementCount = statements.map { s =>
      Try {
        lrs.addStatement(s)
      } match {
        case Failure(_) =>
          logger.warn(s"Can not upload statement: $s")
          0
        case _ => 1
      }
    }.sum
    logger.info(s"Statements were migrate: $successStatementCount of ${statements.size}")
  }

  private def migrateAgentProfiles(implicit session: JdbcBackend#Session) = {
    logger.info("Migrate agent profiles")
    val agentProfiles = dataContext.getAgentProfiles
    val successAgentProfiles = agentProfiles.map { s =>
      Try {
        lrs.addOrUpdateAgentProfile(s.agent, s.profileId, s.content)
      } match {
        case Failure(_) =>
          logger.warn(s"Cann't upload agent profile: $s")
          0
        case _ => 1
      }
    }.sum
    logger.info(s"Agent profiles were migrate: $successAgentProfiles of ${agentProfiles.size}")
  }

  private def migrateActivityProfiles(implicit session: JdbcBackend#Session) = {
    logger.info("Migrate activity profiles")
    val activityProfiles = dataContext.getActivityProfiles
    val successActivityProfiles = activityProfiles.map { s =>
      Try {
        lrs.addOrUpdateActivityProfile(s._1, s._2, s._3)
      } match {
        case Failure(_) =>
          logger.warn(s"Cann't upload activity profile: $s")
          0
        case _ => 1
      }
    }.sum
    logger.info(s"Activity profile were migrate: $successActivityProfiles of ${activityProfiles.size}")
  }

  private def migrateActivityStates(implicit session: JdbcBackend#Session) = {

//    logger.info("Migrate activity states")
//    dataContext.getActivityState.map { s =>
//      Try {
//        lrs.saveProfile(s._1, s._2, s._3, s._4, s._5)
//      } match {
//        case Failure(_) => logger.warn(s"Cann't upload activity state: $s")
//        case _ =>
//      }
//    }
//    logger.info("Activity statements were migrate")
  }
}
