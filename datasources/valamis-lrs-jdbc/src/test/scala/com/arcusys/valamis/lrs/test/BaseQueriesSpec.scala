package com.arcusys.valamis.lrs.test

import java.net.URI

import com.arcusys.json.JsonHelper
import com.arcusys.valamis.lrs.StatementQuery
import com.arcusys.valamis.lrs.utils._
import com.arcusys.valamis.lrs.serializer._
import com.arcusys.valamis.lrs.test.tincan.{Agents, Verbs}
import com.arcusys.valamis.lrs.tincan._
import org.joda.time.DateTime
import org.scalatest._
import com.arcusys.valamis.lrs.jdbc.database.api._

import scala.util._

/**
 * Created by Iliya Tryapitsin on 25.06.15.
 */
class BaseQueriesSpec (module: BaseCoreModule)
  extends BaseDatabaseSpec (module)
  with FeatureSpecLike {

  import driver.simple._
  import lrs._

  agents foreach { pair =>
    scenario (pair._1) {
      val json = JsonHelper.toJson(pair._2.asInstanceOf[AnyRef])
      val agent = JsonHelper.fromJson[Agent](json, new AgentSerializer)

      db withTransaction { implicit session =>

        lrs.actors add agent
      }
    }
  }

  agents foreach { pair =>
    scenario (pair._1 + " and find it") {
      val json = JsonHelper.toJson(pair._2.asInstanceOf[AnyRef])
      val agent = JsonHelper.fromJson[Agent](json, new AgentSerializer)

      db withTransaction { implicit session =>

        val k = lrs.actors keyFor agent
        assert(k isDefined)
      }
    }
  }

  statements foreach { pair =>
    scenario (pair._1) {
      val json = JsonHelper.toJson(pair._2.asInstanceOf[AnyRef])
      val statement = JsonHelper.fromJson[Statement](json, new StatementSerializer)

      db withTransaction { implicit session =>

        val k = lrs.statements add statement

        val result = lrs.findStatements(StatementQuery(statementId = k.?))
        k
      } afterThat { statementKey =>
        logger.info(s"Statement key = $statementKey")
      }
    }
  }

  contexts foreach { pair =>
    scenario (pair._1) {
      val json = JsonHelper.toJson(pair._2.asInstanceOf[AnyRef])
      val context = JsonHelper.fromJson[Context](json, new ContextSerializer)

      db withTransaction { implicit session =>
        lrs.contexts add context

      } afterThat { key =>
        logger.info(s"Context key = $key")
      }
    }
  }

  activities foreach { pair =>
    scenario(pair._1) {
      val json = JsonHelper.toJson(pair._2.asInstanceOf[AnyRef])
      val activity = JsonHelper.fromJson[Activity](json, new ActivitySerializer)

      db withTransaction { implicit session =>

        val key = lrs.activities addOrUpdate activity
        val activities = lrs.activities.filter { x =>
          x.id === activity.id
        } run

        assert(activities.length == 1)
        key

      } afterThat { key =>
        logger.info(s"Activity key = $key")
      }
    }
  }

  scenario ("Test statement search") {

    db withTransaction { implicit session =>
      for (i <- 1 to 50) {
        statements map { case (s, a) =>
          Try {
            val json = JsonHelper.toJson(a.asInstanceOf[AnyRef])
            val statement = JsonHelper.fromJson[Statement](json, new StatementSerializer)

            lrs.addStatement(statement)
          } match {
            case Success (_) => 1
            case Failure (e) => 0
          }
        } sum
      }
    }

    val startTime = DateTime.now()
    val result    = lrs findStatements StatementQuery( verb = new URI(Verbs.validUri) ?)
    val endTime   = DateTime.now()
    logger.info(s"Load ${result.seq.size} statements in ${endTime.getMillisOfDay - startTime.getMillisOfDay} ms")

    val json = JsonHelper.toJson(Agents.Good.`should pass agent typical`)
    val agent = JsonHelper.fromJson[Agent](json, new AgentSerializer)
    val result1 = valamisReporter.findMaxActivityScaled(agent, Verbs.validUri )
    assert(result1.length > 0)
  }
}