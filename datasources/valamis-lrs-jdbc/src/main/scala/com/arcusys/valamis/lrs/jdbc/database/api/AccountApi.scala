package com.arcusys.valamis.lrs.jdbc.database.api

import com.arcusys.valamis.lrs.jdbc.database.converter._
import com.arcusys.valamis.lrs.jdbc.database.LrsDataContext
import com.arcusys.valamis.lrs.jdbc.database.api.query.AccountQueries
import com.arcusys.valamis.lrs.jdbc.database.row.AccountRow
import com.arcusys.valamis.lrs.tincan.Account

/**
 * LRS component for a Tincan [[Account]]
 * @author Created by Iliya Tryapitsin on 08.07.15.
 */
trait AccountApi extends AccountQueries {
  this: LrsDataContext =>

  import driver.simple._

  /**
   * Load Tincan [[Account]] by storage key
   * @param key Storage key
   * @param session
   * @return Tincan [[Account]] instance
   */
  def findAccountByKey (key: AccountRow#KeyType)
                       (implicit session: Session) = key map { x =>
    accounts filter {
      y => y.key === x
    } first
  }

  implicit class AccountsInsertQueries (q: AccountQ) {

    /**
     * Find key for Account in storage
     * @param account Account instance
     * @param session
     * @return Key or None if instance did not save
     */
    def keyFor (account: Account)
               (implicit session: Session) = findKeyQC (account.name, account.homePage) firstOption

    /**
     * Insert Tincan Account to the storage
     * @param account Account instance
     * @param session
     * @return Identity key in the storage
     */
    def add (account: Account)
            (implicit session: Session): AccountRow#Type =
      q returning q.map { x =>
        x.key
      } += account.convert

    /**
     * Insert Tincan Account to the storage
     * @param account Account instance
     * @param session
     * @return Identity key in the storage
     */
    def add (account: Option[Account])
            (implicit session: Session): AccountRow#KeyType = account map { v => q add v }

    /**
     * Insert unique Tincan Account to the storage
     * @param account Account instance
     * @param session
     * @return Identity key in the storage
     */
    def addUnique (account: Account)
                  (implicit session: Session): AccountRow#Type =
      keyFor (account) getOrElse { q add account }

    /**
     * Insert unique Tincan Account to the storage
     * @param account Account instance
     * @param session
     * @return Identity key in the storage
     */
    def addUnique (account: Option[Account])
                  (implicit session: Session): AccountRow#KeyType = account map { v => q addUnique v }
  }
}
