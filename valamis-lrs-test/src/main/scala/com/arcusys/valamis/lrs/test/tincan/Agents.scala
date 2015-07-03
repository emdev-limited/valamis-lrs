package com.arcusys.valamis.lrs.test.tincan

/**
 * Created by Iliya Tryapitsin on 12/02/15.
 */

case class Agent(objectType: Option[String] = None,
                 name: Option[String] = None,
                 mbox: Option[String] = None,
                 mbox_sha1sum: Option[String] = None,
                 openid: Option[String] = None,
                 account: Option[Account] = None) extends Actor

object Agents {
  private val mBox = Some("mailto:conformancetest@tincanapi.com")
  private val mBoxSha1Sum = Some("db77b9104b531ecbb0b967f6942549d0ba80fda1")
  private val openId = Some("http://openid.tincanapi.com")
  private val mBoxQuery = Some("mailto:conformancetest+query@tincanapi.com")
  private val mBoxSha1SumQuery = Some("6954e807cfbfc5b375d185de32f05de269f93d6f")
  private val openIdQuery = Some("http://openid.tincanapi.com/query")
  private val objectType = Some("Agent")
  private val name = Some("xAPI account")

  object Bad {
    val nameNull = Some(typical.get.copy(name = Some("null")))
  }

  object Good {
    val `should pass agent typical`                = typical
    val `should pass agent with name, mbox & type` = Some(Agent(name = name, mbox = mBox, objectType = objectType))
    val `should pass agent with mbox       & type` = Some(Agent(mbox = mBox, objectType = objectType))
    val `should pass agent with mbox sna1  & type` = Some(Agent(mbox_sha1sum = mBoxSha1Sum, objectType = objectType))
    val `should pass agent with openid     & type` = Some(Agent(openid = openId, objectType = objectType))
    val `should pass agent with account    & type` = Some(Agent(account = AgentAccounts.typical, objectType = objectType))
    val `should pass agent with mbox      only`    = Some(Agent(mbox = mBox))
    val `should pass agent with mbox sha1 only`    = Some(Agent(mbox_sha1sum = mBoxSha1Sum))
    val `should pass agent with opneid    only`    = Some(Agent(openid = openId))
    val `should pass agent with account   only`    = Some(Agent(account = AgentAccounts.typical))
  }

  val empty = Some(Agent())
  val typical = Some(Agent(mbox = mBox, objectType = objectType))
  val mboxAndType = Some(Agent(mbox = mBox, objectType = objectType))
  val mboxSha1AndType = Some(Agent(mbox_sha1sum = mBoxSha1Sum, objectType = objectType))
  val openidAndType = Some(Agent(openid = openId, objectType = objectType))
  val accountAndType = Some(Agent(account = AgentAccounts.typical, objectType = objectType))
  val mboxOnly = Some(Agent(mbox = mBox))
  val mboxSha1Only = Some(Agent(mbox_sha1sum = mBoxSha1Sum))
  val openidOnly = Some(Agent(openid = openId))
  val accountOnly = Some(Agent(account = AgentAccounts.typical))
  val forQueryMbox = Some(Agent(mbox = mBox))
  val forQueryMboxSha1 = Some(Agent(mbox_sha1sum = mBoxSha1Sum))
  val forQueryOpenid = Some(Agent(openid = openIdQuery))
  val forQueryAccount = Some(Agent(account = AgentAccounts.typical))

  val oneTypicalInSeq = Some(Seq(Agents.typical.get))
  val oneMboxOnlyInSeq = Some(Seq(Agents.mboxOnly.get))
  val oneMboxSha1OnlyInSeq = Some(Seq(Agents.mboxSha1Only.get))
  val oneOpenidOnlyInSeq = Some(Seq(Agents.openidOnly.get))
  val oneAccountOnlyInSeq = Some(Seq(Agents.accountOnly.get))
  val twoTypicalInSeq = Some(Seq(Agents.typical.get, Agents.typical.get))
}

