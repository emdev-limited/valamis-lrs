package com.arcusys.valamis.lrs.jdbc.database.converter

import com.arcusys.valamis.lrs._
import com.arcusys.valamis.lrs.jdbc.database.row._
import com.arcusys.valamis.lrs.security.{Application, Token}
import com.arcusys.valamis.lrs.tincan._

/**
 * Created by Iliya Tryapitsin on 30.06.15.
 */
trait ToTincanConverter {

  implicit class StatementRecExtension (rec: StatementRow) {

    case class Builder(actor:       Option[Actor]   = None,
                       obj:         Option[StatementObject] = None,
                       result:      Option[Result]  = None,
                       context:     Option[Context] = None,
                       authority:   Option[Actor]   = None,
                       attachments: Seq[Attachment] = Seq() ) {

      def withObj         (arg: => StatementObject) = copy (obj         = arg ?)
      def withActor       (arg: => Actor          ) = copy (actor       = arg ?)
      def withResult      (arg: => Option[Result] ) = copy (result      = arg)
      def withContext     (arg: => Option[Context]) = copy (context     = arg)
      def withAuthority   (arg: => Option[Actor]  ) = copy (authority   = arg)
      def withAttachments (arg: => Seq[Attachment]) = copy (attachments = arg)

      def build = Statement(
        id          = rec.key.toUUID ?,
        obj         = obj get,
        verb        = Verb (rec.verbId, rec.verbDisplay),
        actor       = actor get,
        result      = result,
        stored      = rec.stored,
        context     = context,
        version     = rec.version,
        authority   = authority,
        timestamp   = rec.timestamp,
        attachments = attachments
      )
    }

    def convert = Builder()
  }

  implicit class ResultRecExtension (rec: ResultRow) {

    case class Builder (score: Option[Score] = None) {
      def withScore (arg: Option[Score]) = copy (score = arg)

      def build = Result(
        score      = score,
        success    = rec.success,
        completion = rec.completion,
        response   = rec.response,
        duration   = rec.duration,
        extensions = rec.extensions
      )
    }

    def convert = Builder()
  }

  implicit class ScoreRecExtension(rec: ScoreRow) {
    def convert: Score = Score(
      scaled = rec.scaled,
      raw    = rec.raw,
      min    = rec.min,
      max    = rec.max
    )
  }

  implicit class ContextRecExtension (rec: ContextRow) {
    case class Builder (instructor:    Option[Actor] = None,
                                team:          Option[Group] = None,
                                contextActivities: Option[ContextActivities] = None,
                                statement:     Option[StatementReference] = None) {
      def withInstructor (arg: Option[Actor]) = copy (instructor = arg)
      def withTeam (arg: Option[Group]) = copy (team = arg)
      def withContextActivities (arg: Option[ContextActivities]) = copy (contextActivities = arg)
      def withStatement (arg: Option[StatementReference]) = copy (statement = arg)

      def build = Context(
        registration = rec.registration.map { x => x.toUUID },
        instructor   = instructor,
        team         = team,
        contextActivities = contextActivities,
        revision     = rec.revision,
        platform     = rec.platform,
        language     = rec.language,
        statement    = statement,
        extensions   = rec.extensions
      )
    }

    def convert = Builder()
  }

  implicit class ActivityRecExtension (rec: ActivityRow) {

    def convert = Activity(
      rec.id,
      rec.name,
      rec.description,
      rec.theType,
      rec.moreInfo,
      rec.interactionType,
      rec.correctResponsesPattern,
      rec.choices,
      rec.scale,
      rec.source,
      rec.target,
      rec.steps,
      rec.extensions)
  }

  implicit class AttachmentRecExtension (rec: AttachmentRow) {

    def convert = Attachment(
      description = rec.description,
      contentType = rec.content,
      usageType   = rec.usageType,
      display     = rec.display,
      fileUrl     = rec.fileUrl,
      length      = rec.length,
      sha2        = rec.sha2
    )
  }

  implicit class AccountRecExtension(rec: AccountRow) {
    def convert: Account = Account(
      homePage = rec.homepage,
      name     = rec.name
    )
  }

  implicit class ActorRecExtension (rec: ActorRow) {
    case class Builder (account: Option[Account] = None,
                                members: Seq[Agent] = Seq()) {

      def withAccount (arg: Option[Account]) = copy (account = arg)

      def withAccount (arg: Account) = copy (account = arg ?)

      def withMembers (arg: Seq[Agent]) = copy (members = arg)

      def build = rec match {
        case a: AgentRow => buildAgent
        case g: GroupRow => buildGroup
      }

      def buildGroup = Group(
        account = account,
        name = rec.name,
        member = members ?,
        mBox = rec.mBox,
        mBoxSha1Sum = rec.mBoxSha1Sum,
        openId = rec.openId)

      def buildAgent = Agent(
        account = account,
        name = rec.name,
        mBox = rec.mBox,
        mBoxSha1Sum = rec.mBoxSha1Sum,
        openId = rec.openId)
    }

    def convert = Builder()
  }

  implicit class StatementRefRecExtension (rec: StatementReferenceRow) {
    def convert = rec.statementId.toUUID afterThat { uuid => StatementReference(uuid) }
  }

  implicit class SubStatementRecExtension (rec: SubStatementRow) {

    case class Builder(actor: Option[Actor] = None,
                       obj:   Option[StatementObject] = None) {
      def withActor (arg: => Actor) = copy (actor = arg ?)

      def withStatementObject (arg: => StatementObject) = copy (obj = arg ?)

      def build = SubStatement(
        actor = actor get,
        verb = Verb(rec.verbId, rec.verbDisplay),
        obj = obj get
      )
    }

    def convert = Builder()
  }

  implicit class DocumentRecExtension (rec: DocumentRow) {
    def convert = Document(rec.key toUUID, rec.updated, rec.contents, rec.cType)
  }

  implicit class ApplicationRecExtension (rec: ApplicationRow) {
    def convert = Application(rec.appId, rec.name, rec.description, rec.appSecret, rec.scope, rec.regDateTime, rec.isActive, rec.authType)
  }

  implicit class TokenRecExtension (rec: TokenRow) {
    def convert = Token(rec.userKey, rec.applicationKey, rec.code, rec.codeSecret, rec.callback,rec.issueAt, rec.verifier, rec.token, rec.tokenSecret)
  }
}
