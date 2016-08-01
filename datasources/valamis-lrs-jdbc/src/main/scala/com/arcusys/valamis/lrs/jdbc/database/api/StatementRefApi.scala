package com.arcusys.valamis.lrs.jdbc.database.api

import java.util.UUID

import com.arcusys.valamis.lrs.jdbc.database.converter._
import com.arcusys.valamis.lrs.jdbc.database.api.query.TypeAliases
import com.arcusys.valamis.lrs.jdbc.database.row.StatementReferenceRow
import com.arcusys.valamis.lrs.jdbc.database.LrsDataContext
import com.arcusys.valamis.lrs.jdbc.database.api.query.StatementRefQueries
import com.arcusys.valamis.lrs.jdbc.database.row.StatementReferenceRow
import com.arcusys.valamis.lrs.tincan.StatementReference

/**
 * LRS component for a Tincan [[StatementReference]]
 * @author Created by Iliya Tryapitsin on 08.07.15.
 */
trait StatementRefApi extends StatementRefQueries {
  this: LrsDataContext
    with SubStatementApi
    with StatementApi
    with StatementObjectApi
    with ActorApi
    with ActivityApi =>

  import driver.simple._

  /**
   * Find list of [[StatementReference]]s by list of storage keys
   * @param keys Storage keys list
   * @param session
   * @return List of Tincan [[StatementReference]] with storage key
   */
  def findStatementRefsByKeys (keys: Seq[StatementReferenceRow#Type])
                              (implicit session: Session): Map[StatementReferenceRow#Type, StatementReference] =
    if (keys isEmpty) Map()
    else  findStatementRefsByKeysQ (keys).run map { x => (x.key, x convert) } toMap

  /**
   * Find Statement Reference by storage key
   * @param key Storage key
   * @return Tincan Statement Reference
   */
  def findStatementRefByKey (key: StatementReferenceRow#Type)
                            (implicit session: Session): StatementReference =
    findStatementRefByKeyQ (key).first convert

  implicit class StatementRefInsertQueries (q: StatementRefQ) {

    /**
     * Find Statement Reference Key by Statement Id
     * @param id Statement Id
     * @param session
     * @return Key or None
     */
    def keyFor (id: String)
               (implicit session: Session): Option[StatementReferenceRow#Type] =
      findStatementRefByIdQC(id).firstOption map {
        x => x.key
      }

    /**
     * Find Statement Reference Key for instance
     * @param s Statement Reference instance
     * @param session
     * @return Key or None
     */
    def keyFor (s: StatementReference)
               (implicit session: Session): Option[StatementReferenceRow#Type] =
      q keyFor s.id

    /**
     * Find Statement Reference Key by Statement Id
     * @param id Statement Id
     * @param session
     * @return Key or None
     */
    def keyFor (id: UUID)
               (implicit session: Session): Option[StatementReferenceRow#Type] =
      q keyFor id.toString

    /**
     * Check is exists Statement Reference instance in data storage by Statement Id
     * @param id Statement Id
     * @param session
     * @return True - exists
     */
    def existsFor (id: UUID)
                  (implicit session: Session): Boolean =
      q keyFor id isDefined

    /**
     * Insert Tincan Statement reference to the storage
     * @param s Statement reference instance
     * @param session
     * @return Identity key in the storage
     */
    def add (s: StatementReference)
            (implicit session: Session): StatementReferenceRow#Type =
      s.convert withKey {
        statementObjects add s

      } withStatementKey {
        s.id.toString

      } build { r =>
        q += r
      }

    /**
     * Insert unique Tincan Statement reference to the storage
     * @param s Statement reference instance
     * @param session
     * @return Identity key in the storage
     */
    def addUnique (s: StatementReference)
                  (implicit session: Session): StatementReferenceRow#Type =
      q keyFor s getOrElse { q add s }

    /**
     * Insert unique Tincan Statement reference to the storage
     * @param s Statement reference instance
     * @param session
     * @return Identity key in the storage
     */
    def addUnique (s: Option[StatementReference])
                  (implicit session: Session): Option[StatementReferenceRow#Type] =
      s map { x => q addUnique x }
  }
}
