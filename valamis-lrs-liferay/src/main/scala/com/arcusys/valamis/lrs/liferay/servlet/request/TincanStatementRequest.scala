package com.arcusys.valamis.lrs.liferay.servlet.request

import java.net.URI
import java.util.UUID
import javax.servlet.http.HttpServletRequest

import com.arcusys.valamis.lrs.serializer.{AgentSerializer, StatementSerializer}
import com.arcusys.valamis.lrs.tincan.{Agent => ag, FormatType, Statement}
import com.arcusys.json.JsonHelper
import org.joda.time.DateTime

import scala.util.{Failure, Success, Try}

/**
 * Created by Iliya Tryapitsin on 29/12/14.
 */
class TincanStatementRequest(r: HttpServletRequest)
  extends BaseLrsRequest(r)
  with BaseTincanPartialRequestComponent {

  import TincanStatementRequest._

  def isRequestingSingleStatement = statementId.isDefined | voidedStatementId.isDefined

  def statementId = optional(TincanStatementRequest.StatementId) match {
    case Some(value) => Some(UUID.fromString(value))
    case None => None
  }

  def voidedStatementId = optional(TincanStatementRequest.VoidedStatementId) match {
    case Some(value) => Some(UUID.fromString(value))
    case None => None
  }

  def agent = optional(Agent) match {
    case Some(value) => Some(JsonHelper.fromJson[ag](value, new AgentSerializer))
    case None => None
  }

  def verb = optional(Verb) match {
    case Some(value) => Some(new URI(value))
    case None => None
  }

  def activity = optional(Activity) match {
    case Some(value) => Some(new URI(value))
    case None => None
  }

  def registration = optional(Registration) match {
    case Some(value) => Some(UUID.fromString(value))
    case None => None
  }

  def relatedActivities = optional(RelatedActivities) match {
    case Some(value) => Try(value.toBoolean) match {
      case Success(ra) => ra
      case Failure(_) => false
    }
    case None => false
  }

  def relatedAgents = optional(RelatedAgents) match {
    case Some(value) => Try(value.toBoolean) match {
      case Success(ra) => ra
      case Failure(_) => false
    }
    case None => false
  }

  // TODO: Is duplicate since method in BaseTincanSinceRequestComponent?
  def since = optional(Since) match {
    case Some(value) => Try(DateTime.parse(value)) match {
      case Success(ra) => Some(ra)
      case Failure(_) => None
    }
    case None => None
  }

  def until = optional(Until) match {
    case Some(value) => Try(DateTime.parse(value)) match {
      case Success(ra) => Some(ra)
      case Failure(_) => None
    }
    case None => None
  }

  def format = optional(Format) match {
    case Some(value) => FormatType.withName(value.toLowerCase)
    case None => FormatType.Exact
  }

  def attachments = optional(Attachments) match {
    case Some(value) => Try(value.toBoolean) match {
      case Success(ra) => ra
      case Failure(_) => false
    }
    case None => false
  }

  def ascending = optional(Ascending) match {
    case Some(value) => Try(value.toBoolean) match {
      case Success(ra) => ra
      case Failure(_) => false
    }
    case None => false
  }

  def statements = {
    bodyContent = body
    val content = if (bodyContent.startsWith("[")) bodyContent
    else s"[$bodyContent]"
    JsonHelper.fromJson[Seq[Statement]](content, new StatementSerializer())
  }

  private var bodyContent = ""

  private def getParam(name: String) = request.getParameter(name) match {
    case null => ""
    case str  => s"$name=$str&"
  }

  def toMoreIRL =
    s"""${request.getRequestURI}?
        |${getParam(StatementId)}
        |${getParam(VoidedStatementId)}
        |${getParam(Agent)}
        |${getParam(Verb)}
        |${getParam(Activity)}
        |${getParam(Registration)}
        |${getParam(Since)}
        |${getParam(Until)}
        |${getParam(RelatedActivities)}
        |${getParam(RelatedAgents)}
        |${getParam(Limit)}
        |offset=${offset + limit}&
        |${getParam(Format)}
        |${getParam(Attachments)}
        |${getParam(Ascending)}"""
      .stripMargin
      .replace(" ", "")
      .replace("\t", "")
      .replace("\n", "")

  override def toString =
    s"""
      |TincanStatementRequest instance
      |statementId       = $statementId
      |voidedStatementId = $voidedStatementId
      |agent             = $agent
      |verb              = $verb
      |activity          = $activity
      |registration      = $registration
      |relatedActivities = $relatedActivities
      |relatedAgents     = $relatedAgents
      |since             = $since
      |until             = $until
      |limit             = $limit
      |format            = $format
      |attachments       = $attachments
      |ascending         = $ascending
      |statements        = $statements
      |body              = $bodyContent"""
      .stripMargin
}

object TincanStatementRequest {
  val StatementId       = "statementId"
  val VoidedStatementId = "voidedStatementId"
  val Agent             = "agent"
  val Verb              = "verb"
  val Activity          = "activity"
  val Registration      = "registration"
  val Since             = "since"
  val Until             = "until"
  val RelatedActivities = "related_activities"
  val RelatedAgents     = "related_agents"
  val Attachments       = "attachments"
  val Ascending         = "ascending"
  val Format            = "format"
}
