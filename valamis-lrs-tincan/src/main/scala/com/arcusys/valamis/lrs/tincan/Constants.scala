package com.arcusys.valamis.lrs.tincan

/**
 * Created by Iliya Tryapitsin on 25/02/15.
 */
object Constants {
  val Empty                = ""

  object Headers {
    val Version            = "X-Experience-API-Version"
  }

  object Content {
    val Json               = "JSONContent"
    val Other              = "OtherContent"
  }

  val Ids                  = "ids"
  val Exact                = "exact"
  val Canonical            = "canonical"

  object InteractionType {
    val Choice             = "choice"
    val Likert             = "likert"
    val Matching           = "matching"
    val Sequencing         = "sequencing"
    val Performance        = "performance"
    val LongFillIn         = "long-fill-in"
    val TrueFalse          = "true-false"
    val Numeric            = "numeric"
    val FillIn             = "fill-in"
    val Other              = "other"
  }

  val Package              = "package"
  val Course               = "course"

  val Registration         = "registration"
  val Since                = "since"
  val Until                = "until"

  object Tincan {
    val Verb               = "verb"
    val Actor              = "actor"
    val Score              = "score"
    val Limit              = "limit"
    val Offset             = "offset"
    val Agent              = "agent"
    val Group              = "group"
    val Person             = "person"
    val Format             = "format"
    val Result             = "result"
    val Activity           = "activity"
    val Statement          = "statement"
    val Ascending          = "ascending"
    val VoidedVerb         = "http://adlnet.gov/expapi/verbs/voided"
    val SubStatement       = "subStatement"
    val RelatedAgents      = "related_agents"
    val ContextActivities  = "contextActivities"
    val RelatedActivities  = "related_activities"
    val StatementReference = "statementRef"

    object Field {
      val descriptor = "descriptor"
      val ObjectType = "objectType"
      val `object` = "object"
      val obj = "obj"
      val Id = "id"
      val ContextField = "context"
      val Timestamp = "timestamp"
      val authority = "authority"
      val Stored = "stored"
      val version = "version"
      val Attachments = "attachments"
      val name = "name"
      val mBox = "mbox"
      val mBoxSha1Sum = "mbox_sha1sum"
      val openId = "openid"
      val account = "account"
      val member = "member"
      val homePage = "homePage"
      val team = "team"
      val registration = "registration"
      val instructor = "instructor"
      val revision = "revision"
      val platform = "platform"
      val language = "language"
      val extensions = "extensions"
      val `type`  = "type"
      val description = "description"
      val moreInfo = "moreInfo"
      val interactionType = "interactionType"
      val correctResponsesPattern = "correctResponsesPattern"
      val choices = "choices"
      val scale = "scale"
      val source = "source"
      val target = "target"
      val steps = "steps"
      val definition = "definition"
      val success = "success"
      val completion = "completion"
      val response = "response"
      val duration = "duration"
      val scaled = "scaled"
      val raw = "raw"
      val min = "min"
      val max = "max"
      val display = "display"
    }

  }

}
