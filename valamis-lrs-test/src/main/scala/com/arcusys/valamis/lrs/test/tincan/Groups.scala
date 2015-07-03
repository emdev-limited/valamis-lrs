package com.arcusys.valamis.lrs.test.tincan

/**
 * Created by Iliya Tryapitsin on 12/02/15.
 */

case class Group(objectType: Option[String] = None,
                 name: Option[String] = None,
                 mbox: Option[String] = None,
                 mbox_sha1sum: Option[String] = None,
                 openid: Option[String] = None,
                 account: Option[Account] = None,
                 storedId: Option[Long] = None,
                 member: Option[Seq[Agent]] = None) extends Actor

object Groups {
  private val mbox = Some("mailto:conformancetest-group@tincanapi.com")
  private val name = Some("test group")
  private val mbox_sha1sum = Some("4e271041e78101311fb37284ef1a1d35c3f1db35")
  private val openid = Some("http://group.openid.tincanapi.com")
  private val objectType = Some("Group")

  object Good {
  }

  val empty = Some(Group())
  val typical = Some(Group(mbox = mbox, objectType = objectType))
  val mboxAndType = Some(Group(mbox = mbox, objectType = objectType))
  val mboxSha1AndType = Some(Group(mbox_sha1sum = mbox_sha1sum, objectType = objectType))
  val openidAndType = Some(Group(openid = openid, objectType = objectType))
  val accountAndType = Some(Group(account = AgentAccounts.typical, objectType = objectType))

  val mboxTypeAndName = Some(Group(objectType = objectType, mbox = mbox, name = name))
  val mboxTypeAndMember = Some(Group(mbox = mbox, objectType = objectType, member = Agents.oneTypicalInSeq))

  val mboxSha1TypeAndMember = Some(Group(mbox = mbox, mbox_sha1sum = mbox_sha1sum, objectType = objectType, member = Agents.oneTypicalInSeq))
  val mboxSha1TypeAndName = Some(Group(objectType = objectType, mbox_sha1sum = mbox_sha1sum, name = name))

  val openidTypeAndName = Some(Group(openid = openid, objectType = objectType, name = name))
  val openidTypeAndMember = Some(Group(openid = openid, objectType = objectType, member = Agents.oneTypicalInSeq))

  val accountTypeAndName = Some(Group(account = AgentAccounts.typical, objectType = objectType, name = name))
  val accountTypeAndMember = Some(Group(account = AgentAccounts.typical, objectType = objectType, member = Agents.oneTypicalInSeq))

  val allPropertiesTypicalAgentMember = Some(Group(mbox = mbox, objectType = objectType, name = name, member = Agents.oneTypicalInSeq))
  val allPropertiesMboxAgentMember = Some(Group(mbox = mbox, objectType = objectType, name = name, member = Agents.oneMboxOnlyInSeq))
  val allPropertiesMboxSha1AgentMember = Some(Group(mbox = mbox, objectType = objectType, name = name, member = Agents.oneMboxSha1OnlyInSeq))
  val allPropertiesOpenidAgentMember = Some(Group(mbox = mbox, objectType = objectType, name = name, member = Agents.oneOpenidOnlyInSeq))
  val allPropertiesAccountAgentMember = Some(Group(mbox = mbox, objectType = objectType, name = name, member = Agents.oneAccountOnlyInSeq))
  val allPropertiesTwoTypicalAgentsMember = Some(Group(mbox = mbox, objectType = objectType, name = name, member = Agents.twoTypicalInSeq))
}
