package com.arcusys.valamis.lrs.jdbc.database.api.query

import com.arcusys.valamis.lrs.jdbc.database.LrsDataContext
import com.arcusys.valamis.lrs.jdbc.database.schema.ActorsSchema

import scala.slick.lifted

/**
 * Created by Iliya Tryapitsin on 08.07.15.
 */
trait TypeAliases {
  this: LrsDataContext =>

  val TakeCount = 10

  type ScoreQ           = lifted.Query[ScoresTable,               ScoresTable#TableElementType,             Seq]
  type ActorQ           = lifted.Query[ActorsTable,               ActorsTable#TableElementType,             Seq]
  type ResultQ          = lifted.Query[ResultsTable,              ResultsTable#TableElementType,            Seq]
  type AccountQ         = lifted.Query[AccountsTable,             AccountsTable#TableElementType,           Seq]
  type ContextQ         = lifted.Query[ContextsTable,             ContextsTable#TableElementType,           Seq]
  type DocumentQ        = lifted.Query[DocumentsTable,            DocumentsTable#TableElementType,          Seq]
  type ActivityQ        = lifted.Query[ActivitiesTable,           ActivitiesTable#TableElementType,         Seq]
  type StatementQ       = lifted.Query[StatementsTable,           StatementsTable#TableElementType,         Seq]
  type AttachmentQ      = lifted.Query[AttachmentsTable,          AttachmentsTable#TableElementType,        Seq]
  type SubStatementQ    = lifted.Query[SubStatementsTable,        SubStatementsTable#TableElementType,      Seq]
  type StatementObjQ    = lifted.Query[StatementObjectsTable,     StatementObjectsTable#TableElementType,   Seq]
  type StatementRefQ    = lifted.Query[StatementReferenceTable,   StatementReferenceTable#TableElementType, Seq]
  type ContextActivityQ = lifted.Query[ContextActivitiesTable,    ContextActivitiesTable#TableElementType,  Seq]
}
