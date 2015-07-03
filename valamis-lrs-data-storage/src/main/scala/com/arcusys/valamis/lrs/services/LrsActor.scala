package com.arcusys.valamis.lrs.services

import java.util.UUID

import akka.actor.Actor
import com.arcusys.valamis.lrs.tincan._
import org.joda.time.DateTime

import scala.slick.driver.JdbcDriver
import scala.slick.jdbc.JdbcBackend


/**
 * Created by Iliya Tryapitsin on 23.04.15.
 */

object LrsAction {
  case class AddStatement (statement: Statement     )
  case class FindStatement(query:     StatementQuery)
  case class GetPerson    (agent:     Agent         )
  case class DeleteProfile(agent:     Agent,
                           profileId: String        )
  case class GetProfileContent(agent:     Agent,
                               profileId: String    )
  case class GetProfiles  (agent:        Option[Agent],
                           stateId:      Option[String] = None,
                           activityId:   Option[String] = None,
                           registration: Option[UUID]   = None,
                           since:        Option[DateTime] = None)

  case class GetProfileIds (activityId:  String,
                            since:       Option[DateTime] = None)

  case class DeleteProfiles(agent:       Agent,
                            activityId:  String,
                            registration:Option[UUID] = None)

  case class SaveDocument  (agent:        Agent,
                            doc:          Document,
                            activityId:   Option[String] = None,
                            stateId:      Option[String] = None,
                            registration: Option[UUID]   = None,
                            profileId:    Option[String] = None) {
    require(profileId.isDefined || (activityId.isDefined && stateId.isDefined))
  }
  case class DeleteProfileActivityWithDocument(activityId: String,
                                               profileId:  String)

  case class GetActivityById    (activityId: String)
  case class GetActivitiesByName(activity:   String)
  case class GetDocument        (activityId: String,
                                 profileId:  String)
}

class LrsActor(driver: JdbcDriver,
               db:     JdbcBackend#Database ) extends Actor {
  import LrsAction._

  val lrs = new LRS(driver, db)

  override def receive: Receive = {
    case AddStatement(statement) => lrs.addStatement(statement)
    case FindStatement(query)    => lrs.findStatements(query)
    case GetPerson(agent)        => lrs.getPerson(agent)
    case DeleteProfile(agent, profileId)      => lrs.deleteProfile(agent, profileId)
    case GetProfileContent(agent, profileId)  => lrs.getProfileContent(agent, profileId)
  }
}
