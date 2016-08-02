package com.arcusys.valamis.lrs.jdbc.database

import com.arcusys.valamis.lrs.jdbc.database.converter.{ToTincanConverter, ToRowConverter}
import com.arcusys.valamis.lrs.jdbc.database.schema._

import scala.concurrent.duration.Duration
import scala.slick.driver._
import scala.slick.jdbc.JdbcBackend

/**
 * Created by Iliya Tryapitsin on 10/01/15.
 */
trait LrsDataContext extends BaseDataContext
  with AccountSchema
  with ActivityProfileSchema
  with ActivitySchema
  with ActorsSchema
  with AgentProfileSchema
  with AttachmentSchema
  with ContextActivitySchema
  with ContextActivityActivitySchema
  with ContextActivityContextSchema
  with ContextSchema
  with DocumentSchema
  with ResultSchema
  with ScoreSchema
  with StatementObjectSchema
  with StatementReferenceSchema
  with StatementSchema
  with StateProfileSchema
  with SubStatementSchema
  with ToRowConverter
  with ToTincanConverter {

  //TODO: Change this value before release
  implicit val timeout = Duration.Inf
}
