package com.arcusys.valamis.lrs.jdbc.database.api.query

import com.arcusys.valamis.lrs.jdbc.database.LrsDataContext
import com.arcusys.valamis.lrs.jdbc.database.row.StatementRow
import org.joda.time.DateTime

/**
 * Created by Iliya Tryapitsin on 08.07.15.
 */
trait StatementQueries extends TypeAliases {
  this: LrsDataContext =>

  import jodaSupport._
  import driver.simple._

  private type KeyType = StatementRow#Type
  private type IdCol   = ConstColumn[StatementRow#Type]
  private type DateCol = ConstColumn[DateTime]
  private type NumCol  = ConstColumn[Long]

  /**
   * Find statement by Id
   * @param id Id
   * @return Statement storage query
   */
  private def findByIdQ (id: IdCol) = statements filter { x => x.key like id }

  private def findByKeysQ (id: Seq[KeyType]) = statements filter { x => x.key inSet id }

  /**
   * Find actor which assigned with statement
   * @param id Statement id
   * @return Statement storage query
   */
  private def findActorByIdQ (id: IdCol) = findByIdQ (id) flatMap { x => x.actor }

  /**
   * Find statement reference by statement id
   * @param id Statement id
   * @return Statement reference storage query
   */
  private def findStatementRefByIdQ (id: IdCol) = statementReferences filter { x => x.statementId === id  }

  /**
   * Find result which assigned with statement
   * @param id Statement id
   * @return Statement storage query
   */
  private def findResultByIdQ (id: IdCol) = findByIdQ (id) flatMap { x => x.result }

  /**
   * Find context which assigned with statement
   * @param id Statement id
   * @return Statement storage query
   */
  private def findContextByIdQ (id: IdCol) = findByIdQ (id) flatMap { x => x.context }

  /**
   * Find authority which assigned with statement
   * @param id Statement id
   * @return Statement storage query
   */
  private def findAuthorityByIdQ (id: IdCol) = findByIdQ (id) flatMap { x => x.authority }

  /**
   * Find attachments which assigned with statement
   * @param id Statement id
   * @return Attachment storage query
   */
  private def findAttachmentsByIdQ (id: IdCol) = attachments filter { x => x.statementKey === id }

  /**
   * Find statements since date. Sorted by timestamp
   * @param date Start date
   * @return Statement storage query
   */
  def findSinceQ (date: DateCol) =
    statements filter {
      x => x.timestamp >= date
    } sortBy {
      x => x.timestamp
    }

  def findStatementsQ = statements map { x => x }

  def findStatementsCountQ = findStatementsQ length

  def findResultsByStatementKeysQ(keys: Seq[KeyType]) =
    findByKeysQ (keys) join results on {
      (x1, x2) => x1.resultKey === x2.key
    } map {
      case (x1, x2) => (x1.key, x2)
    }

  def findContextsByStatementKeysQ(keys: Seq[KeyType]) =
    findByKeysQ (keys) join contexts on {
      (x1, x2) => x1.contextKey === x2.key
    } map {
      case (x1, x2) => (x1.key, x2)
    }

  def findAttachmentsByStatementKeysQ(keys: Seq[KeyType]) = attachments filter { x => x.statementKey inSet keys }

  /**
   * Take top statements
   * @param count Take count
   * @return Statement storage query
   */
  def takeTopQ (count: Int) = statements sortBy { x => x.timestamp.desc } take count

  /**
   * Compiled query find statement by Id
   */
  val findStatementByIdQC = Compiled((id: IdCol) => findByIdQ (id))

  /**
   * Compiled query find statement reference by Id
   */
  val findStatementRefByIdQC = Compiled((id: IdCol) => findStatementRefByIdQ(id))

  /**
   * Compiled query find actor which assigned with statement
   */
  val findActorByStatementIdQC = Compiled((id: IdCol) => findActorByIdQ (id))

  /**
   * Compiled query find result which assigned with statement
   */
  val findResultByStatementIdQC = Compiled((id: IdCol) => findResultByIdQ (id))

  /**
   * Compiled query find context which assigned with statement
   */
  val findContextByStatementIdQC = Compiled((id: IdCol) => findContextByIdQ (id))

  /**
   * Compiled query find authority which assigned with statement
   */
  val findAuthorityByStatementIdQC = Compiled((id: IdCol) => findAuthorityByIdQ (id))

  /**
   * Compiled query find attachments which assigned with statement
   */
  val findAttachmentsByStatementIdQC = Compiled((id: IdCol) => findAttachmentsByIdQ (id))

  /**
   * Compiled query find statements since date. Sorted by timestamp
   */
  val findStatementsSinceDateQC = Compiled((date: DateCol) => findSinceQ (date))

  /**
   * Compiled query take top statements
   */
//  val takeTopStatementsQC = Compiled((count: Int) => takeTopQ (count))

  val findStatementsQC = Compiled(findStatementsQ)
}
