package com.arcusys.valamis.lrs.jdbc.database.converter

import com.arcusys.valamis.lrs.jdbc.database.row._
import com.arcusys.valamis.lrs.security.{Token, Application}
import com.arcusys.valamis.lrs.tincan._
import com.arcusys.valamis.lrs._
import com.arcusys.valamis.lrs.utils._

/**
 * Created by Iliya Tryapitsin on 30.06.15.
 */

trait ToRowConverter {

  trait StatementObj {
    def toStatementObj: StatementObjectRow
  }

  implicit class DocumentExtension (d: Document) {
    def convert = Builder()
    def toRow = DocumentRow(
      key      = d.id.toString,
      contents = d.contents,
      cType    = d.cType
    )

    case class Builder (aKey: Option[ActorRow#Type] = None) {
      def withAgent (arg: ActorRow#Type) = copy(arg ?)
      def build (act: (DocumentRow) => DocumentRow#Type) = act(
        DocumentRow(
          key      = d.id.toString,
          contents = d.contents,
          cType    = d.cType
        )
      )
    }
  }

  implicit class StatementExtension(s: Statement) {
    def convert = Builder()

    case class Builder(aKey: Option[ActorRow#Type]      = None,
                       oKey: StatementObjectRow#KeyType = None,
                       rKey: ResultRow#KeyType          = None,
                       cKey: ContextRow#KeyType         = None,
                       acKey: Option[ActorRow#Type]     = None){
      def withObj (arg: StatementObjectRow#Type)     = copy(oKey  = arg ?)
      def withActor (arg: ActorRow#Type)             = copy(acKey = arg ?)
      def withResult (arg: Option[ResultRow#Type])   = copy(rKey  = arg  )
      def withContext (arg: Option[ContextRow#Type]) = copy(cKey  = arg  )
      def withAuthority (arg: Option[ActorRow#Type]) = copy(aKey  = arg  )

      def build (act: (StatementRow) => Unit): StatementRow#Type = {
        StatementRow(
          key          = s.id.get.toString,
          actorKey     = acKey get,
          verbId       = s.verb.id,
          verbDisplay  = s.verb.display,
          objectKey    = oKey get,
          resultKey    = rKey,
          contextKey   = cKey,
          timestamp    = s.timestamp,
          stored       = s.stored,
          authorityKey = aKey,
          version      = s.version
        ) afterThat act

        s.id.get.toString
      }
    }
  }

  implicit class ContextActivityExtension(contextActivities: ContextActivities) {
    def convert = Builder()

    case class Builder(id: ContextRow#KeyType = None) {
      def withContext(arg: ContextRow#Type) = copy(arg ?)

      def build(activities: Seq[(String, Option[ActivityRow#Type])]) = {
        contextActivities.category.map { x =>
          (ContextActivityType.Category, x)

        } ++ contextActivities.grouping.map { x =>
          (ContextActivityType.Grouping, x)

        } ++ contextActivities.other.map { x =>
          (ContextActivityType.Other, x)

        } ++ contextActivities.parent.map { x =>
          (ContextActivityType.Parent, x)

        } map { case (tpe, x) =>
          val key = activities.find(_._1 == x.id).get._2.get
          (tpe, key, id.get) // tpe, activityKey, contentKey
        }
      }
    }
  }

  implicit class ContextExtension(context: Context) {
    def convert = Builder()

    case class Builder(iKey: Option[ActorRow#Type] = None,
                       tKey: Option[ActorRow#Type] = None,
                       rKey: Option[StatementReferenceRow#Type] = None ) {
      def withInstructor (arg: Option[ActorRow#Type])       = copy(arg, tKey, rKey)
      def withTeam (arg: Option[ActorRow#Type])             = copy(iKey, arg, rKey)
      def withRef (arg: Option[StatementReferenceRow#Type]) = copy(iKey, tKey, arg)
      def build (act: (ContextRow) => Unit): ContextRow#Type = {
        val rec = ContextRow(
          instructor = iKey,
          team       = tKey,
          revision   = context.revision,
          platform   = context.platform,
          language   = context.language,
          statement  = rKey,
          extensions = context.extensions
        )
        rec afterThat act
        rec.key get
      }
    }
  }

  implicit class StatementObjExtension(obj: StatementObject) extends StatementObj {
    override def toStatementObj: StatementObjectRow =
      StatementObjectRow(
        objectType = StatementObjectType.withType(obj)
      )
  }

  implicit class ActorExtension(actor: Actor) extends StatementObj {

    override def toStatementObj = StatementObjectRow(
      objectType = StatementObjectType.withType(actor)
    )

    def convert = Builder()

    protected case class Builder(key:    Option[StatementObjectRow#Type] = None,
                                 accKey: AccountRow#KeyType    = None,
                                 grKey:  Option[GroupRow#Type] = None) {

      def withKey(act: => StatementObjectRow#Type) = copy(act ?, accKey, grKey)

      def withAccount(aKey: => Option[AccountRow#Type]) = copy(key, aKey, grKey)

      def withGroup(gKey: GroupRow#Type) = actor match {
        case a: Agent => copy(key, accKey, gKey ?)
        case g: Group => copy()
      }

      def build(act: (ActorRow) => Unit) = {
        act(
          actor match {
            case _: Agent => AgentRow(
              key get,
              actor.name,
              actor.mBox,
              actor.mBoxSha1Sum,
              actor.openId,
              accKey,
              grKey)

            case _: Group => GroupRow(
              key get,
              actor.name,
              actor.mBox,
              actor.mBoxSha1Sum,
              actor.openId,
              accKey)
          }
        )
        key get
      }
    }
  }

  implicit class SubStatementExtension(s: SubStatement) {

    def convert = Builder()

    case class Builder(key:  Option[StatementObjectRow#Type] = None,
                       oKey: Option[StatementObjectRow#Type] = None,
                       aKey: Option[ActorRow#Type] = None) {
      def withObjectKey(act: => StatementObjectRow#Type) = copy(key, act ?, aKey)

      def withActorKey(act: => StatementObjectRow#Type) = copy(key, oKey, act ?)

      def withKey(act: => StatementObjectRow#Type) = copy(act ?, oKey, aKey)

      def build(act: (SubStatementRow) => StatementObjectRow#Type) = {
        act(
          SubStatementRow(
            key         = key  get,
            objectKey   = oKey get,
            actorKey    = aKey get,
            verbId      = s.verb.id,
            verbDisplay = s.verb.display
          )
        )

        key get
      }
    }
  }

  implicit class StatementRefExtension(s: StatementReference) {
    def convert = Builder()

    case class Builder(key:  Option[StatementObjectRow#Type] = None,
                       sKey: Option[StatementRow#Type]       = None) {
      def withKey(act: => StatementObjectRow#Type) = copy(act ?, sKey)

      def withStatementKey(act: => StatementRow#Type) = copy(key, act ?)

      def build(act: (StatementReferenceRow) => StatementObjectRow#Type) = {
        act(
          StatementReferenceRow(key get, sKey get)
        )

        key get
      }
    }
  }

  implicit class ActivityExtension(activity: Activity) {

    case class Builder(key: StatementObjectRow#KeyType = None) {
      def withKey (arg: StatementObjectRow#Type) = copy(arg ?)

      def build (act: (ActivityRow) => Unit) =  {
        act(
          ActivityRow(
            key        = key get,
            id         = activity.id,
            name       = activity.name,
            description= activity.description,
            theType    = activity.theType,
            moreInfo   = activity.moreInfo,
            interactionType = activity.interactionType,
            correctResponsesPattern = activity.correctResponsesPattern,
            choices    = activity.choices,
            scale      = activity.scale,
            source     = activity.source,
            target     = activity.target,
            steps      = activity.steps,
            extensions = activity.extensions
          )
        )

        key get
      }

      def build =
        ActivityRow(
          key        = key get,
          id         = activity.id,
          name       = activity.name,
          description= activity.description,
          theType    = activity.theType,
          moreInfo   = activity.moreInfo,
          interactionType = activity.interactionType,
          correctResponsesPattern = activity.correctResponsesPattern,
          choices    = activity.choices,
          scale      = activity.scale,
          source     = activity.source,
          target     = activity.target,
          steps      = activity.steps,
          extensions = activity.extensions
        )
    }

    def convert = Builder()
  }

  implicit class AccountExtension(account: Account) {
    
    def convert: AccountRow = AccountRow(
      homepage = account.homePage,
      name     = account.name
    )
  }

  implicit class ResultExtension(result: Result) {
    def convert = new {
      def withScoreKey(act: => ScoreRow#KeyType) =
        ResultRow(
          scoreId    = act,
          success    = result.success,
          completion = result.completion,
          response   = result.response,
          duration   = result.duration,
          extensions = result.extensions
        )
    }
  }

  implicit class ScoreExtension(score: Score) {
    def convert: ScoreRow = ScoreRow(
      scaled = score.scaled,
      raw    = score.raw,
      min    = score.min,
      max    = score.max
    )
  }

  implicit class AttachmentExtension (attachment: Attachment) {

    case class Builder(sKey: Option[StatementRow#Type] = None) {
      def withStatement (arg: StatementRow#Type) = copy(arg ?)

      def build =  AttachmentRow(
        statementId = sKey get,
        usageType   = attachment.usageType,
        display     = attachment.display,
        description = attachment.description,
        content     = attachment.contentType,
        length      = attachment.length,
        sha2        = attachment.sha2,
        fileUrl     = attachment.fileUrl
      )
    }

    def convert = Builder()
  }

  implicit class ApplicationExtension(app: Application) {
    def convert: ApplicationRow = ApplicationRow(
      appId       = app.appId      ,
      name        = app.name       ,
      description = app.description,
      appSecret   = app.appSecret  ,
      scope       = app.scope      ,
      regDateTime = app.regDateTime,
      isActive    = app.isActive   ,
      authType    = app.authType
    )
  }


  implicit class TokenExtension(app: Token) {
    def convert: TokenRow = TokenRow(
      userKey        = app.userKey,
      applicationKey = app.applicationKey,
      code           = app.code,
      codeSecret     = app.codeSecret,
      callback       = app.callback,
      issueAt        = app.issueAt,
      verifier       = app.verifier,
      token          = app.token,
      tokenSecret    = app.tokenSecret
    )
  }
}
