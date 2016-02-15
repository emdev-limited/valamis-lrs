package com.arcusys.valamis.lrs.jdbc.database.row

/**
 * Created by Iliya Tryapitsin on 11/01/15.
 */
trait ActorRow extends WithRequireKey[StatementObjectRow#Type] {

  def name: Option[String]

  def mBox: Option[String]

  def mBoxSha1Sum: Option[String]

  def openId: Option[String]

  def accountKey: AccountRow#KeyType
}