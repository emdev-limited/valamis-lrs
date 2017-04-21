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

  import driver.simple._
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

    def actorKey    = column [ActorRow#Type]          ("actorId"    )
    def objectKey   = column [StatementObjectRow#Type]("objectKey"  )
    def verbId      = column [String]                 ("verbId"     , O.Length(2000, varying = true)) // TODO: Change type to [[java.net.URI]]
    def verbDisplay = column [LanguageMap]            ("verbDisplay", O.Length(2000, varying = true))

    def resultKey   = column [?[ResultRow#Type]]      ("resultId"   )
    def authorityKey= column [?[ActorRow#Type]]       ("authorityId")
    def contextKey  = column [?[ContextRow#Type]]     ("contextId"  , O.DBType(uuidKeyLength))
    def version     = column [?[TincanVersion.Type]]  ("version"    , O.Length(50, varying = true))

    def timestamp   = column [DateTime] ("timestamp")
    def stored      = column [DateTime] ("stored"   )

    def actor           = foreignKey (fkName("stmnt2actor"   ), actorKey    , TableQuery[ActorsTable])          (_.key)
    def authority       = foreignKey (fkName("stmnt2authrity"), authorityKey, TableQuery[ActorsTable])          (_.key)
    def result          = foreignKey (fkName("stmnt2result"  ), resultKey   , TableQuery[ResultsTable])         (_.key)
    def context         = foreignKey (fkName("stmnt2cntxt"   ), contextKey  , TableQuery[ContextsTable])        (_.key)
    def statementObject = foreignKey (fkName("stmnt2stmntObj"), objectKey   , TableQuery[StatementObjectsTable])(_.key)

    def actorKeyIndx = index("idx_actor_key", actorKey)
    def authorityIndx = index("idx_authority_key", authorityKey)
    def resultIndx = index("idx_result_key",  resultKey)
    def contextIndx = index("idx_contextKey_key",  contextKey)
    def objectIndx = index("idx_object_key",  objectKey)
  }

  lazy val statements = TableQuery[StatementsTable]

}
