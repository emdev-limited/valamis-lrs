package com.arcusys.valamis.lrs.liferay.history.ver230.from

import com.arcusys.valamis.lrs.tincan._
import org.joda.time.DateTime

import scala.slick.jdbc.JdbcBackend
import com.arcusys.valamis.lrs.liferay.history.BaseComponent
import com.arcusys.valamis.lrs.liferay.history.Helper._

/**
 * Created by Iliya Tryapitsin on 17/03/15.
 */
class DataContext extends BaseComponent
with AttachmentComponent
with StatementComponent
with ActivityComponent
with ActivityProfileComponent
with ActivityStateComponent
with ActorComponent
with ResultComponent
with ContextActivityComponent
with AgentProfileComponent
with ContextComponent
with DocumentComponent
with StatementRefComponent
with SubStatementComponent {


  def getStatements(implicit session: JdbcBackend#Session): Seq[Statement] = statements.map { row =>
    require(row.actorKey.isDefined,s"ActorKey should be defined in Statement with Key = ${row.key}")
    require(row.objType.isDefined, s"ObjType  should be defined in Statement with Key = ${row.key}")
    require(row.objKey.isDefined,  s"ObjKey   should be defined in Statement with Key = ${row.key}")
    require(row.verbId.isDefined,  s"VerbId   should be defined in Statement with Key = ${row.key}")

    Statement(
      id = getUUIDOption(row.id),
      actor = getActor(row.actorKey.get),
      verb = getVerb(row.verbId, row.verbDisplay),
      obj = getStatementObject(row.objKey.get, row.objType.get),
      result = getResult(row.resultKey),
      context = getContext(row.contextKey),
      timestamp = row.timestamp getOrElse DateTime.now,
      stored = row.stored getOrElse DateTime.now,
      authority = getActor(row.authorityKey),
      version = row.version.map { x => TincanVersion.withName(x)}.orElse(Some(TincanVersion.ver101)),
      attachments = getAttachments(row.key)
    )
  }

  def getActivityProfiles(implicit session: JdbcBackend#Session): Seq[(String, String, Document)] = activityProfiles.map { s =>
    (s._3, s._4, getDocument(s._2))
  }

  def getAgentProfiles(implicit session: JdbcBackend#Session): Seq[AgentProfile] = agentProfiles.map { row =>
    AgentProfile(row.profileId.get, getAgent(row.agentId.get), getDocument(row.documentId.get))
  }
}