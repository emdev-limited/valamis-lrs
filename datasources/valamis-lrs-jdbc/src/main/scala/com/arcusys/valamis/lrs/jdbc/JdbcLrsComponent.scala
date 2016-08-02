package com.arcusys.valamis.lrs.jdbc

import java.util.UUID

import com.arcusys.valamis.lrs._
import com.arcusys.valamis.lrs.jdbc.database.LrsDataContext
import com.arcusys.valamis.lrs.jdbc.database.row._
import com.arcusys.valamis.lrs.tincan._
import org.joda.time.DateTime

import scala.concurrent.Await

trait JdbcLrsComponent extends LrsComponent
    with LrsDataContext
    with JdbcLrsExtension
    with Loggable {

  import driver.simple._


  lazy val statementStorage = new JdbcStatementStorage
  lazy val activityProfileStorage = new JdbcActivityProfileStorage
  lazy val agentProfileStorage = new JdbcAgentProfileStorage
  lazy val stateProfileStorage = new JdbcStateProfileStorage

  class JdbcStatementStorage extends StatementStorage {
    override def findStatement(id: Statement#Id): Option[Statement] = db withSession { implicit session =>
      if(id.isEmpty)
        throw new IllegalArgumentException("No statement id")
      Await.result(findStatementImpl(id.get.toString), timeout)
    }

    def isVoidedStatement(id: Statement#Id): Boolean = db.withTransaction { implicit session =>
      isVoided(id.toString)
    }

    override def findStatementsByParams(params: StatementQuery): PartialSeq[Statement] = db withSession { implicit session =>
      Await.result(findStatementsByParamsImpl(params), timeout)
    }

    override def addStatement(statement: Statement): UUID = db.withTransaction { implicit session =>
      statements add statement
    }

    override def containStatement(s: Statement): Boolean = db.withTransaction { implicit session =>
      s exists
    }

    override def getActivities(activity: String): Seq[Activity] = db withSession { implicit session =>
      activities filterByName activity
    }

    override def getPerson(agent: Agent): Person = db.withSession(implicit session => {
      val filteredActors = actors
        .leftJoin(accounts).on { (actor, account) => actor.accountKey === account.key }
        .filter { x =>
          var expr = x._1.mBox === agent.mBox        ||
            x._1.mBoxSha1Sum   === agent.mBoxSha1Sum ||
            x._1.openId        === agent.openId

          agent.account.foreach { account => expr = expr ||
            x._2.name     === account.name &&
              x._2.homepage === account.homePage
          }
          expr
        } list

      Person(
        filteredActors collect { case x if x._1.name.isDefined        => x._1.name.get        },
        filteredActors collect { case x if x._1.mBox.isDefined        => x._1.mBox.get        },
        filteredActors collect { case x if x._1.mBoxSha1Sum.isDefined => x._1.mBoxSha1Sum.get },
        filteredActors collect { case x if x._1.openId.isDefined      => x._1.openId.get      },
        filteredActors map     { x => Account(homePage = x._2.homepage, name = x._2.name) }
      )
    })


    override def getActivity(activityId: Activity#Id): Option[Activity] = db withSession { implicit session =>
      findActivityById (activityId)
    }

  }

  class JdbcActivityProfileStorage extends ActivityProfileStorage {
    override def add(activityProfile: ActivityProfile): Unit = db withSession { implicit session =>
      val activityKey = statementObjects addExt Activity(id = activityProfile.activityId)
      val doc = activityProfile.document

      documents.insert(DocumentRow(doc.id.toString, contents = doc.contents, cType = doc.cType))
      activityProfiles.insert(ActivityProfileRow(activityKey, activityProfile.profileId, doc.id.toString))
    }

    override def findBy(activityId: Activity#Id, profileId: ProfileId): Option[Document] = db withSession { implicit session =>
      findDocument(activityId, profileId)
    }

    override def findBy(activityId: Activity#Id, since: Option[DateTime]): Seq[ProfileId] = db withSession { implicit session =>

      since map { dt =>
        findActivityProfileByUpdateAndProfileIdQC (dt, activityId) run

      } getOrElse {
        findActivityProfileByProfileIdQC (activityId) run

      }
    }

    override def update(activityProfile: ActivityProfile): Unit = db.withSession { implicit session =>
      val doc = activityProfile.document
      documents filter { x => x.key === doc.id.toString } update doc.toRow

    }

    override def delete(activityId: Activity#Id, profileId: ProfileId): Unit = db withSession { implicit session =>
      findActivityProfileByActivityIdAndProfileIdQC (activityId, profileId) delete
    }
  }

  class JdbcAgentProfileStorage extends AgentProfileStorage {
    override def add(agentProfile: AgentProfile): Unit = db.withSession { implicit session =>
      val agentKey = statementObjects addExt agentProfile.agent
      
      val doc = agentProfile.content
      documents.insert(DocumentRow(doc.id.toString, contents = doc.contents, cType = doc.cType))
      agentProfiles.insert(AgentProfileRow(agentProfile.profileId, agentKey, doc.id.toString))
    }

    override def findBy(agent: Agent, profileId: ProfileId): Option[Document] = db.withSession { implicit session =>
      actors keyFor agent match {
        case Some(key) => getAgentDocumentRow(key, profileId) map { x => x.toModel }
        case None => None
      }
    }

    override def findBy(agent: Agent, since: Option[DateTime]): Seq[ProfileId] = db.withSession { implicit session =>
      actors keyFor agent match {
        case Some(value) => getAgentDocuments(value, since) map { x => x.key }
        case None        => Seq()
      }
    }

    override def update(agentProfile: AgentProfile): Unit = db.withSession { implicit session =>
      val doc = agentProfile.content
      documents filter { x => x.key === doc.id.toString } update doc.toRow

    }

    override def delete(agent: Agent, profileId: ProfileId): Unit = db withSession { implicit session =>
      actors keyFor agent map { key =>
        deleteDocument     (key, profileId)
        deleteAgentProfile (key, profileId)
      }
    }
  }

  class JdbcStateProfileStorage extends StateProfileStorage {
    override def add(stateProfile: StateProfile): Unit = db withSession { implicit session =>
      val agentKey = statementObjects addExt stateProfile.agent
      val activityKey = statementObjects addExt Activity(id = stateProfile.activityId)
      val doc = stateProfile.document

      documents.insert(DocumentRow(doc.id.toString, contents = doc.contents, cType = doc.cType))
      stateProfiles.insert(StateProfileRow(stateProfile.stateId, agentKey, activityKey, stateProfile.registration.map(_.toString), doc.id.toString))
    }

    override def findBy(agent: Agent, activityId: String, stateId: String, registration: Option[UUID]): Option[Document] = db withSession { implicit session =>
      actors keyFor agent match {
        case Some(actorKey) => findDocument (actorKey, activityId, stateId, registration)
        case None => None
      }
    }

    def findBy(agent:        Agent,
               activityId:   Activity#Id,
               registration: Option[UUID],
               since:        Option[DateTime]): Seq[ProfileId] =
    db.withSession { implicit session =>
      actors keyFor agent match {
        case Some(value) => findDocumentKeys(value, activityId, registration, since)
        case None => Seq()
      }
    }

    override def update(stateProfile: StateProfile): Unit = db.withSession { implicit session =>
      val doc = stateProfile.document
      documents filter { x => x.key === doc.id.toString } update doc.toRow

    }

    override def delete(agent: Agent, activityId: Activity#Id, stateId: String, registration: Option[UUID]): Unit = db withSession { implicit session =>
      actors keyFor agent match {
        case Some(actorKey) =>
          deleteAgentProfile(actorKey, activityId, stateId, registration)

        case None => Unit
      }
    }

    def delete(agent: Agent, activityId: String, registration: Option[UUID]): Unit = db.withSession { implicit session =>
      actors keyFor agent match {
        case Some(key) =>
        //        deleteDocument     (key, profileId) //getDocuments(value, activityId, registration, None) map { x => x.key }
        //        documents filter { x => x.key inSet docKeys } delete
        val docKeys = getDocuments(key, activityId, registration, None) map { x => x.key }

          documents filter { x => x.key inSet docKeys } delete

        case None => Unit
      }
    }
  }

}
