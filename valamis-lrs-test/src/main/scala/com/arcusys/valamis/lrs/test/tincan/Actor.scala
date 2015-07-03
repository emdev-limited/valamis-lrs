package com.arcusys.valamis.lrs.test.tincan

/**
 * Created by Iliya Tryapitsin on 13/02/15.
 */
trait Actor extends StatementObj {
  def name: Option[String]

  def mbox: Option[String]

  def mbox_sha1sum: Option[String]

  def openid: Option[String]

  def account: Option[Account]
}
