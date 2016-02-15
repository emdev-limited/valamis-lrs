package com.arcusys.valamis.lrs.jdbc.database.schema

import com.arcusys.valamis.lrs.jdbc.database.LrsDataContext
import com.arcusys.valamis.lrs.jdbc.database.utils.DbNameUtils
import DbNameUtils._
import com.arcusys.valamis.lrs.jdbc.database.row.ActivityRow
import com.arcusys.valamis.lrs.tincan._

/**
 * Created by Iliya Tryapitsin on 22.07.15.
 */
trait ActivitySchema extends SchemaUtil {
  this: LrsDataContext =>

  import executionContext.driver.simple._
  import ForeignKeyAction._

  class ActivitiesTable(tag: Tag) extends LongKeyTable[ActivityRow](tag, tblName("activities"), false) {

    def * = (
      key,
      id,
      name,
      description,
      theType,
      moreInfo,
      interactionType,
      correctResponses,
      choices,
      scale,
      source,
      target,
      steps,
      extensions
    ) <> (ActivityRow.tupled, ActivityRow.unapply)

    def interactionType  = column[?[InteractionType.Type]]   ("interactionType", O.Nullable)
    def id               = column[String]                    ("id"             , O.Nullable, O.DBType(varCharPk ))
    def name             = column[?[LanguageMap]]            ("name"           , O.Nullable, O.DBType(varCharMax))
    def description      = column[?[LanguageMap]]            ("description"    , O.Nullable, O.DBType(varCharMax))
    def theType          = column[?[String]]                 ("type"           , O.Nullable, O.DBType(varCharMax))
    def moreInfo         = column[?[String]]                 ("moreInfo"       , O.Nullable, O.DBType(varCharMax))
    def correctResponses = column[Seq[String]]               ("crctRespPtrn"   , O.Nullable, O.DBType(varCharMax))
    def choices          = column[Seq[InteractionComponent]] ("choices"        , O.Nullable, O.DBType(varCharMax))
    def scale            = column[Seq[InteractionComponent]] ("scale"          , O.Nullable, O.DBType(varCharMax))
    def source           = column[Seq[InteractionComponent]] ("source"         , O.Nullable, O.DBType(varCharMax))
    def target           = column[Seq[InteractionComponent]] ("target"         , O.Nullable, O.DBType(varCharMax))
    def steps            = column[Seq[InteractionComponent]] ("steps"          , O.Nullable, O.DBType(varCharMax))
    def extensions       = column[?[LanguageMap]]            ("extensions"     , O.Nullable, O.DBType(varCharMax))

    def statementObject = foreignKey(fkName("activity2stmntObj"), key, TQ[StatementObjectsTable])(x => x.key, onDelete = Cascade)

    def indx = index(idxName("activities"), id, unique = true)
  }

  lazy val activities = TQ[ActivitiesTable]
}
