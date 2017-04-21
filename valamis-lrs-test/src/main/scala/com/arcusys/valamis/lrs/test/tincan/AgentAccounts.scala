package com.arcusys.valamis.lrs.test.tincan

import com.arcusys.valamis.lrs.utils._

/**
 * Created by Iliya Tryapitsin on 12/02/15.
 */

case class Account(homePage: Option[String] = None,
                   name: Option[String] = None)

object AgentAccounts {
  val name = Some("test")
  val homePage = "https://tincanapi.com"

  val empty = Some(Account())
  val typical = Some(Account(name = name, homePage = Some(homePage)))
  val consumer = Some(Account(name = Some("oauth_consumer_x75db"), homePage = Some(homePage + "/OAuth/Token")))
  val allProperties = Some(Account(name = name, homePage = Some(homePage)))
  val forQuery = Some(Account(name = Some("forQuery"), homePage = Some(homePage)))

  object Bad {
    val incorrectHomePage = typical map { x => x.copy(homePage = "asadasdasd" ?) }
  }
}
