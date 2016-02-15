package com.arcusys.valamis.lrs.jdbc.database.schema

import com.arcusys.valamis.lrs.jdbc.database.LrsDataContext
import com.arcusys.valamis.lrs.jdbc.database.utils.DbNameUtils
import DbNameUtils._
import com.arcusys.valamis.lrs.jdbc.database.row._
import com.arcusys.valamis.lrs.tincan._
import org.joda.time.DateTime

/**
 * Created by Iliya Tryapitsin on 23.07.15.
 */
trait StatementSchema extends SchemaUtil {
  this: LrsDataContext =>

  import executionContext.driver.simple._
  import jodaSupport._

  class StatementsTable (tag: Tag) extends UUIDKeyTable [StatementRow] (tag, tblName("statements")) {
    override def * = (
      key         ,
      actorKey    ,
      verbId      ,
      verbDisplay ,
      objectKey   ,
      resultKey   ,
      contextKey  ,
      timestamp   ,
      stored      ,
      authorityKey,
      version
    ) <> (StatementRow.tupled, StatementRow.unapply)

    def actorKey    = column [ActorRow#Type]          ("actorId"    , O.NotNull)
    def objectKey   = column [StatementObjectRow#Type]("objectKey"  , O.NotNull)
    def verbId      = column [String]                 ("verbId"     , O.NotNull, O.DBType(varCharMax)) // TODO: Change type to [[java.net.URI]]
    def verbDisplay = column [LanguageMap]            ("verbDisplay", O.NotNull, O.DBType(varCharMax))

    def resultKey   = column [?[ResultRow#Type]]      ("resultId"   , O.Nullable)
    def authorityKey= column [?[ActorRow#Type]]       ("authorityId", O.Nullable)
    def contextKey  = column [?[ContextRow#Type]]     ("contextId"  , O.Nullable, O.DBType(uuidKeyLength))
    def version     = column [?[TincanVersion.Type]]  ("version"    , O.Nullable, O.DBType(varCharMax))

    def timestamp   = column [DateTime] ("timestamp")
    def stored      = column [DateTime] ("stored"   )

    def actor           = foreignKey (fkName("stmnt2actor"   ), actorKey    , TableQuery[ActorsTable])          (_.key)
    def authority       = foreignKey (fkName("stmnt2authrity"), authorityKey, TableQuery[ActorsTable])          (_.key)
    def result          = foreignKey (fkName("stmnt2result"  ), resultKey   , TableQuery[ResultsTable])         (_.key)
    def context         = foreignKey (fkName("stmnt2cntxt"   ), contextKey  , TableQuery[ContextsTable])        (_.key)
    def statementObject = foreignKey (fkName("stmnt2stmntObj"), objectKey   , TableQuery[StatementObjectsTable])(_.key)
  }

  lazy val statements = TableQuery[StatementsTable]

}
