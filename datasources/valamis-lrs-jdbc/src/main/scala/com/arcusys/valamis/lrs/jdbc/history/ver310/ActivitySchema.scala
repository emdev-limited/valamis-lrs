package com.arcusys.valamis.lrs.jdbc.history.ver310

import com.arcusys.valamis.lrs.jdbc.database.BaseDataContext
import com.arcusys.valamis.lrs.jdbc.database.schema.SchemaUtil
import com.arcusys.valamis.lrs.jdbc.database.utils.DbNameUtils._
import com.arcusys.valamis.lrs.tincan._

import scala.slick.driver.JdbcDriver
import scala.slick.jdbc.JdbcBackend

/**
 * Old schema for Activities table
 */
class ActivitySchema(val driver: JdbcDriver, val db: JdbcBackend#Database) extends SchemaUtil with BaseDataContext {

  import driver.simple._

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

    def interactionType  = column[?[InteractionType.Type]]   ("interactionType")
    def id               = column[?[String]]                 ("id"             , O.DBType(varCharPk ))
    def name             = column[?[LanguageMap]]            ("name"           , O.DBType(varCharMax))
    def description      = column[?[LanguageMap]]            ("description"    , O.DBType(varCharMax))
    def theType          = column[?[String]]                 ("type"           , O.DBType(varCharMax))
    def moreInfo         = column[?[String]]                 ("moreInfo"       , O.DBType(varCharMax))
    def correctResponses = column[Seq[String]]               ("crctRespPtrn"   , O.DBType(varCharMax))
    def choices          = column[Seq[InteractionComponent]] ("choices"        , O.DBType(varCharMax))
    def scale            = column[Seq[InteractionComponent]] ("scale"          , O.DBType(varCharMax))
    def source           = column[Seq[InteractionComponent]] ("source"         , O.DBType(varCharMax))
    def target           = column[Seq[InteractionComponent]] ("target"         , O.DBType(varCharMax))
    def steps            = column[Seq[InteractionComponent]] ("steps"          , O.DBType(varCharMax))
    def extensions       = column[?[LanguageMap]]            ("extensions"     , O.DBType(varCharMax))


    def indx = index(idxName("activities"), id, unique = true)
  }

  lazy val activities = TQ[ActivitiesTable]
}
