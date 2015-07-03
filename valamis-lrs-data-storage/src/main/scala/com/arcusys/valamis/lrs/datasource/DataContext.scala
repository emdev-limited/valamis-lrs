package com.arcusys.valamis.lrs.datasource

import com.arcusys.valamis.lrs.datasource.row._
import DbNameUtils._
import com.arcusys.valamis.lrs.tincan.ContentType.{Type => CntType}
import com.arcusys.valamis.lrs.tincan.StatementObjectType.{Type => ObjType}
import com.arcusys.valamis.lrs.tincan.{Constants => C, _}
import com.github.tototoshi.slick.GenericJodaSupport
import com.google.inject.Inject
import org.joda.time.DateTime
import org.slf4j.LoggerFactory

import scala.slick.ast.{Library, LiteralNode}
import scala.slick.driver._
import scala.slick.jdbc.JdbcBackend
import scala.slick.lifted._

/**
 * Created by Iliya Tryapitsin on 10/01/15.
 */
private[lrs] class DataContext @Inject() (val driver: JdbcDriver,
                                          val db:     JdbcBackend#Database)
  extends DataQuery
  with CustomTypeExtension
  with CustomQuery {

  implicit val dr = driver
  protected val jodaSupport = new GenericJodaSupport(driver)

  import jodaSupport._
  import driver.simple._

  abstract class LongKeyTable[E <: {type Type = Long}](tag: Tag, name: String, useAutoInc: Boolean = true) extends Table[E](tag, name) {
    def key = if (useAutoInc)
      column[E#Type]("key", O.PrimaryKey, O.AutoInc)
    else
      column[E#Type]("key", O.PrimaryKey)
  }

  abstract class UUIDKeyTable[E <: {type Type = String}](tag: Tag, name: String) extends Table[E](tag, name) {

    def key = column[String]("key", O.PrimaryKey, O.DBType(uuidKeyLength))
  }

  abstract class StringKeyTable[E <: {type Type = String}](tag: Tag, name: String) extends Table[E](tag, name) {
    def key = column[E#Type]("key", O.PrimaryKey, O.DBType(varCharPk))
  }

  class AccountsTable(tag: Tag) extends LongKeyTable[AccountRow](tag, tblName("accounts")) with TypeMapper {

    def * = (key.?, homepage, name) <>(AccountRow.tupled, AccountRow.unapply)

    def homepage = column[String]("homePage", O.DBType(varCharMax))
    def name     = column[String]("name",     O.DBType(varCharMax))

    def indx = index(idxName("account"), key)
  }

  class ActivitiesTable(tag: Tag) extends LongKeyTable[ActivityRow](tag, tblName("activities"), false) with TypeMapper {

    def * = (key, id, name.?, description.?, theType.?, moreInfo.?, interactionType.?, correctResponsesPattern, choices, scale, source, target, steps, extensions.?) <>
      (ActivityRow.tupled, ActivityRow.unapply)

    def id                      = column[String]                   ("id",              O.Nullable, O.DBType(varCharPk ))
    def name                    = column[LanguageMap]              ("name",            O.Nullable, O.DBType(varCharMax))
    def description             = column[LanguageMap]              ("description",     O.Nullable, O.DBType(varCharMax))
    def theType                 = column[String]                   ("type",            O.Nullable, O.DBType(varCharMax))
    def moreInfo                = column[String]                   ("moreInfo",        O.Nullable, O.DBType(varCharMax))
    def interactionType         = column[InteractionType.Type]     ("interactionType", O.Nullable                      )
    def correctResponsesPattern = column[Seq[String]]              ("crctRespPtrn",    O.Nullable, O.DBType(varCharMax))
    def choices                 = column[Seq[InteractionComponent]]("choices",         O.Nullable, O.DBType(varCharMax))
    def scale                   = column[Seq[InteractionComponent]]("scale",           O.Nullable, O.DBType(varCharMax))
    def source                  = column[Seq[InteractionComponent]]("source",          O.Nullable, O.DBType(varCharMax))
    def target                  = column[Seq[InteractionComponent]]("target",          O.Nullable, O.DBType(varCharMax))
    def steps                   = column[Seq[InteractionComponent]]("steps",           O.Nullable, O.DBType(varCharMax))
    def extensions              = column[Map[String, String]]      ("extensions",      O.Nullable, O.DBType(varCharMax))

    def statementObject = foreignKey(fkName("activity2stmntObj"), key, TableQuery[StatementObjectsTable])(x => x.key, onDelete = ForeignKeyAction.Cascade)

    def indx = index(idxName("activities"), id, unique = true)
  }

  class ActorsTable(tag: Tag) extends LongKeyTable[ActorRow](tag: Tag, tblName("actors"), false)  with TypeMapper {

    override def * = ProvenShape.proveShapeOf(
      (key, name.?, descriptor, mBox.?, mBoxSha1Sum.?, openId.?, accountKey.?, groupKey.?)
        .<>[ActorRow, (ActorRow#Type, Option[String], String, Option[String], Option[String], Option[String], AccountRow#KeyType, Option[GroupRow#Type])](
      {
        case (key, name, actor, mBox, mBoxSha1Sum, openId, accountKey, groupKey) => actor match {
          case C.Tincan.Group => GroupRow(key, name, mBox, mBoxSha1Sum, openId, accountKey).asInstanceOf[ActorRow]
          case _              => AgentRow(key, name, mBox, mBoxSha1Sum, openId, accountKey, groupKey).asInstanceOf[ActorRow]
        }
      }, {
        case AgentRow(key, name, mBox, mBoxSha1Sum, openId, accountKey, groupKey) =>
          Option((key, name, C.Tincan.Agent, mBox, mBoxSha1Sum, openId, accountKey, groupKey))

        case GroupRow(key, name, mBox, mBoxSha1Sum, openId, accountKey) =>
          Option((key, name, C.Tincan.Group, mBox, mBoxSha1Sum, openId, accountKey, None))
      }))

    def account         = foreignKey(fkName("actor2account"),  accountKey, TableQuery[AccountsTable])(account => account.key)
    def group           = foreignKey(fkName("actor2group"),    groupKey,   TableQuery[ActorsTable])(group => group.key)
    def statementObject = foreignKey(fkName("actor2stmntObj"), key,        TableQuery[StatementObjectsTable])(x => x.key)

    def accountKey  = column[AccountRow#Type]("accountKey", O.Nullable)
    def groupKey    = column[GroupRow#Type]("groupKey", O.Nullable)
    def name        = column[String](C.Tincan.Field.name, O.Nullable, O.DBType(varCharMax))
    def descriptor  = column[String](C.Tincan.Field.descriptor, O.NotNull, O.DBType(varCharMax))
    def mBox        = column[String]("mBox", O.Nullable, O.DBType(varCharMax))
    def mBoxSha1Sum = column[String]("mBoxSha1Sum", O.Nullable, O.DBType(varCharMax))
    def openId      = column[String]("openId", O.Nullable, O.DBType(varCharMax))
  }

  class ActivityProfilesTable(tag: Tag) extends Table[ActivityProfileRow](tag, tblName("activityProfiles")) {
    override def *  = (activityKey, profileId, documentKey) <>(ActivityProfileRow.tupled, ActivityProfileRow.unapply)

    def activityKey = column[ActivityRow#Type]("activityKey", O.NotNull                     )
    def profileId   = column[String]          ("profileId"  , O.NotNull, O.DBType(varCharPk))
    def documentKey = column[DocumentRow#Type]("documentKey", O.NotNull, O.DBType(varCharPk))

    def activity = foreignKey(fkName("activityProfile2activity" ), activityKey, TableQuery[ActivitiesTable])(x => x.key, onUpdate = ForeignKeyAction.Restrict, onDelete = ForeignKeyAction.Cascade)
    def document = foreignKey(fkName("activityProfiles2document"), documentKey, TableQuery[DocumentsTable ])(x => x.key, onUpdate = ForeignKeyAction.Restrict, onDelete = ForeignKeyAction.Cascade)

    def indx = index(idxName("activityProfile"), (activityKey, profileId), unique = true)
  }

  class AgentProfilesTable(tag: Tag) extends Table[AgentProfileRow](tag, tblName("agentProfiles")) with TypeMapper {

    def * = (profileId, agentKey, documentKey) <>(AgentProfileRow.tupled, AgentProfileRow.unapply)

    def profileId   = column[String]("profileId", O.NotNull, O.DBType(varCharPk))
    def agentKey    = column[AgentRow#Type]("agentKey")
    def documentKey = column[DocumentRow#Type]("documentKey", O.DBType(varCharPk))

    def document = foreignKey(fkName("agentProfile2document"), documentKey, TableQuery[DocumentsTable])(_.key, onUpdate = ForeignKeyAction.Restrict, onDelete = ForeignKeyAction.Cascade)
    def agent    = foreignKey(fkName("agentProfile2agent"),    agentKey,    TableQuery[ActorsTable])   (_.key, onUpdate = ForeignKeyAction.Restrict, onDelete = ForeignKeyAction.Cascade)

    def indx = index(idxName("agentProfile"), (profileId, agentKey, documentKey), unique = true)
  }

  class AttachmentsTable(tag: Tag) extends LongKeyTable[AttachmentRow](tag: Tag, tblName("attachments")) with TypeMapper {

    override def * = (key.?, statementKey, usageType, display, description.?, content, length, sha2, fileUrl.?) <>(AttachmentRow.tupled, AttachmentRow.unapply)

    def statementKey = column[StatementRow#Type]("statementId", O.NotNull, O.DBType(varCharPk))
    def usageType    = column[String]("usageType", O.DBType(varCharMax))
    def display      = column[LanguageMap]("display", O.DBType(varCharMax))
    def description  = column[LanguageMap]("description", O.DBType(varCharMax), O.Nullable)
    def content      = column[String]("content", O.DBType(varCharMax))
    def length       = column[Int]("length")
    def sha2         = column[String]("sha2", O.DBType(varCharMax))
    def fileUrl      = column[String]("fileUrl", O.Nullable, O.DBType(varCharMax))

    def statement = foreignKey(fkName("atchmnt2stmnt"), statementKey, TableQuery[StatementsTable])(statement => statement.key)
  }

  class ContextActivitiesTable(tag: Tag) extends Table[ContextActivityRow](tag: Tag, tblName("contextActivities")) with TypeMapper {
    override def * = (contextKey, activityKey, contextActivityType) <>(ContextActivityRow.tupled, ContextActivityRow.unapply)

    def contextKey = column[ContextRow#Type]("contextKey", O.NotNull, O.DBType(varCharPk))
    def activityKey = column[ActivityRow#Type]("activityKey", O.NotNull)
    def contextActivityType = column[ContextActivityType.Type]("type", O.NotNull, O.DBType(varCharPk))

    def pk = primaryKey(pkName("contextActivities"), (contextKey, activityKey, contextActivityType))

    def context = foreignKey(fkName("cntxtActvt2cntxt"), contextKey, TableQuery[ContextsTable])(x => x.key)
    def activity = foreignKey(fkName("cntxtActvt2actvt"), activityKey, TableQuery[ActivitiesTable])(x => x.key)
  }

  class ContextsTable(tag: Tag) extends UUIDKeyTable[ContextRow](tag: Tag, tblName("contexts")) with TypeMapper {
    override def * = (key.?, instructor.?, team.?, revision.?, platform.?, language.?, statementRefId.?, extensions.?) <>(ContextRow.tupled, ContextRow.unapply)

    def instructor = column[ActorRow#Type]("instructor", O.Nullable)
    def team = column[GroupRow#Type]("team", O.Nullable)
    def revision = column[String]("revision", O.Nullable, O.DBType(varCharMax))
    def platform = column[String]("platform", O.Nullable, O.DBType(varCharMax))
    def language = column[String]("language", O.Nullable, O.DBType(varCharMax))
    def statementRefId = column[StatementReferenceRow#Type]("statementRefId", O.Nullable)
    def extensions = column[ExtensionMap]("extensions", O.Nullable, O.DBType(varCharMax))

    def statementRef = foreignKey(fkName("cntxt2stmntRef"), statementRefId, TableQuery[StatementReferenceTable])(x => x.key)
  }

  class DocumentsTable(tag: Tag) extends UUIDKeyTable[DocumentRow](tag, tblName("documents")) with TypeMapper {

    def * = (key, updated, contents, cType) <>(DocumentRow.tupled, DocumentRow.unapply)

    def updated  = column[DateTime]("updated")
    def contents = column[String]("contents", O.DBType(varCharMax))
    def cType    = column[CntType]("cType", O.DBType(varCharMax))
  }

  class ResultsTable(tag: Tag) extends LongKeyTable[ResultRow](tag, tblName("results")) with TypeMapper {
    def * = (key.?, scoreKey, success.?, completion.?, response.?, duration.?, extensions.?) <>(ResultRow.tupled, ResultRow.unapply)

    def scoreKey   = column[ScoreRow#KeyType]("scoreKey", O.Nullable)
    def success    = column[Boolean]("success", O.Nullable)
    def completion = column[Boolean]("completion", O.Nullable)
    def response   = column[String]("response", O.Nullable, O.DBType(varCharMax))
    def duration   = column[String]("duration", O.Nullable, O.DBType(varCharMax))
    def extensions = column[Map[String, String]]("extensions", O.Nullable, O.DBType(varCharMax))

    def score = foreignKey(fkName("result2score"), scoreKey, TableQuery[ScoresTable])(score => score.key)
  }

  class ScoresTable(tag: Tag) extends LongKeyTable[ScoreRow](tag, tblName("scores")) {

    def * = (key.?, scaled.?, raw.?, min.?, max.?) <>(ScoreRow.tupled, ScoreRow.unapply)

    def scaled = column[Float]("scaled", O.Nullable)
    def raw    = column[Float]("raw"   , O.Nullable)
    def min    = column[Float]("_min"  , O.Nullable)
    def max    = column[Float]("_max"  , O.Nullable)
  }

  class StatementObjectsTable(tag: Tag) extends LongKeyTable[StatementObjectRow](tag, tblName("statementObjects")) with TypeMapper {

    def objectType = column[ObjType]("objectType", O.NotNull, O.DBType(varCharMax))

    def * = (key.?, objectType) <>(StatementObjectRow.tupled, StatementObjectRow.unapply)
  }

  class StatementReferenceTable(tag: Tag) extends LongKeyTable[StatementReferenceRow](tag, tblName("stmntRefs"), false) with TypeMapper {
    override def * = (key, statementId) <>(StatementReferenceRow.tupled, StatementReferenceRow.unapply)

    def statementId = column[StatementRow#Type]("statementId", O.NotNull, O.DBType(uuidKeyLength))

    def statementObject = foreignKey(fkName("stmntRef2stmntObj"), key, TableQuery[StatementObjectsTable])(statement => statement.key)
//    def statement       = foreignKey(fkName("stmnt2stmntRef")   , statementId, TableQuery[StatementsTable])(statement => statement.key)

    def indx = index(idxName("stmntRef"), (key, statementId))
  }

  class StatementsTable(tag: Tag) extends UUIDKeyTable[StatementRow](tag, tblName("statements")) with TypeMapper {
    override def * = (key, actorKey, verbId, verbDisplay, objectKey, resultKey.?, contextKey.?, timestamp, stored, authorityKey.?, version.?) <>(StatementRow.tupled, StatementRow.unapply)

    def actorKey    = column[ActorRow#Type]("actorId", O.NotNull)
    def verbId      = column[String]("verbId", O.NotNull, O.DBType(varCharMax))
    def verbDisplay = column[LanguageMap]("verbDisplay", O.NotNull, O.DBType(varCharMax))
    def objectKey   = column[StatementObjectRow#Type]("objectKey", O.NotNull)
    def resultKey   = column[ResultRow#Type]("resultId", O.Nullable)
    def contextKey  = column[ContextRow#Type]("contextId", O.Nullable, O.DBType(uuidKeyLength))
    def timestamp   = column[DateTime]("timestamp")
    def stored      = column[DateTime]("stored")
    def authorityKey= column[ActorRow#Type]("authorityId", O.Nullable)
    def version     = column[TincanVersion.Type]("version", O.Nullable, O.DBType(varCharMax))

    def actor           = foreignKey(fkName("stmnt2actor"   ), actorKey,   TableQuery[ActorsTable])(_.key)
    def result          = foreignKey(fkName("stmnt2result"  ), resultKey,  TableQuery[ResultsTable])(_.key)
    def context         = foreignKey(fkName("stmnt2cntxt"   ), contextKey, TableQuery[ContextsTable])(x => x.key)
    def statementObject = foreignKey(fkName("stmnt2stmntObj"), objectKey,  TableQuery[StatementObjectsTable])(x => x.key)
  }

  class StateProfilesTable(tag: Tag) extends Table[StateProfileRow](tag, tblName("stateProfiles")) {
    override def * = (stateId, agentKey, activityKey, registration, documentKey) <> (StateProfileRow.tupled, StateProfileRow.unapply)

    def agentKey = column[AgentRow#Type]("agentKey", O.NotNull)
    def activityKey = column[ActivityRow#Type]("activityKey", O.NotNull)
    def stateId = column[String]("stateId", O.NotNull, O.DBType(varCharPk))
    def registration = column[Option[String]]("registration", O.Nullable, O.DBType(varCharPk))
    def documentKey = column[DocumentRow#Type]("documentKey", O.NotNull , O.DBType(uuidKeyLength))

    def activity = foreignKey(fkName("stateProfile2activity"), activityKey, TableQuery[ActivitiesTable])(x => x.key, onUpdate = ForeignKeyAction.Restrict, onDelete = ForeignKeyAction.Cascade)
    def document = foreignKey(fkName("stateProfiles2document"), documentKey, TableQuery[DocumentsTable])(x => x.key, onUpdate = ForeignKeyAction.Restrict, onDelete = ForeignKeyAction.Cascade)
    def agent = foreignKey(fkName("stateProfile2agent"), agentKey, TableQuery[ActorsTable])(_.key)

    def indx = index(idxName("stateProfile"), (agentKey, activityKey, stateId, registration), unique = true)
  }

  class SubStatementsTable(tag: Tag) extends LongKeyTable[SubStatementRow](tag, tblName("subStatements"), false) with TypeMapper {
    override def * = (key, statementObjectKey, actorKey, verbId, verbDisplay) <>(SubStatementRow.tupled, SubStatementRow.unapply)

    def statementObjectKey = column[StatementObjectRow#Type]("statementObject")
    def actorKey = column[ActorRow#Type]("actorId", O.NotNull)
    def verbId = column[String]("verbId", O.NotNull, O.DBType(varCharMax))
    def verbDisplay = column[LanguageMap]("verbDisplay", O.NotNull, O.DBType(varCharMax))

    def actor = foreignKey(fkName("subStmnt2actor"), actorKey, TableQuery[ActorsTable])(_.key)
    def statementObject = foreignKey(fkName("subSstmnt2stmntObj"), statementObjectKey, TableQuery[StatementObjectsTable])(_.key)
  }

  class VersionTable(tag: Tag) extends Table[Version](tag, tblName("migrations")) with TypeMapper {
    override def * = (migrationName, appliedDate) <>(Version.tupled, Version.unapply)

    def migrationName = column[String]("currentVersion", O.NotNull, O.DBType(varCharMax))

    def appliedDate = column[DateTime]("appliedDate", O.NotNull)
  }

  lazy val accounts          = TableQuery[AccountsTable            ]
  lazy val activities        = TableQuery[ActivitiesTable          ]
  lazy val activityProfiles  = TableQuery[ActivityProfilesTable    ]
  lazy val actors            = TableQuery[ActorsTable              ]
  lazy val contexts          = TableQuery[ContextsTable            ]
  lazy val attachments       = TableQuery[AttachmentsTable         ]
  lazy val contextActivities = TableQuery[ContextActivitiesTable   ]
  lazy val documents         = TableQuery[DocumentsTable           ]
  lazy val results           = TableQuery[ResultsTable             ]
  lazy val scores            = TableQuery[ScoresTable              ]
  lazy val statementObjects  = TableQuery[StatementObjectsTable    ]
  lazy val statementReferences = TableQuery[StatementReferenceTable]
  lazy val statements        = TableQuery[StatementsTable          ]
  lazy val versions          = TableQuery[VersionTable             ]
  lazy val subStatements     = TableQuery[SubStatementsTable       ]
  lazy val agentProfiles     = TableQuery[AgentProfilesTable       ]
  lazy val stateProfiles     = TableQuery[StateProfilesTable       ]

  lazy val getAgents         = actors.filter(x => x.descriptor === "agent")
  lazy val getGroups         = actors.filter(x => x.descriptor === "group")
}
