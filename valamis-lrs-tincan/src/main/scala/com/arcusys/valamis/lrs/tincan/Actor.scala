package com.arcusys.valamis.lrs.tincan

import com.arcusys.valamis.lrs.validator.{AccountValidator, ActorValidator}


/**
 * A mandatory Agent or Group Object
 */
sealed trait Actor extends StatementObject {
  val name:         Option[String]
  val mBox:         Option[String]
  val mBoxSha1Sum:  Option[String]
  val openId:       Option[String]
  val account:      Option[Account]
}

/**
 * An Agent (an individual) is a persona or system.
 * @param name Full name of the Agent.
 * @param mBox The required format is "mailto:email address".
 *             Only email addresses that have only ever been and will ever be assigned to this Agent,
 *             but no others, should be used for this property and mbox_sha1sum.
 * @param mBoxSha1Sum The SHA1 hash of a mailto IRI (i.e. the value of an mbox property).
 *                     An LRS MAY include Agents with a matching hash when a request is based on an mbox.
 * @param openId An openID that uniquely identifies the Agent.
 * @param account An openID that uniquely identifies the Agent.
 */
case class Agent(name:        Option[String] = None,
                 mBox:        Option[String] = None,
                 mBoxSha1Sum: Option[String] = None,
                 openId:      Option[String] = None, // URI, IRI, IRL figure it out later!!!
                 account:     Option[Account] = None,
                 @deprecated
                 storedId:    Option[Long] = None) extends Actor {

  ActorValidator check this

  override def toString =
    s"""
       |Agent instance
       |name        = $name
       |mBox        = $mBox
       |mBoxSha1Sum = $mBoxSha1Sum
       |openId      = $openId
       |account     = $account
       |storedId    = $storedId
     """.stripMargin
}

/**
 * A Group represents a collection of Agents and can be used in most of the same situations an Agent can be used.
 * There are two types of Groups, anonymous and identified.
 * @param name Name of the group.
 * @param member The members of this Group.
 * @param mBox he required format is "mailto:email address".
 *             Only email addresses that have only ever been and will ever be assigned to this Agent,
 *             but no others, should be used for this property and mbox_sha1sum.
 * @param mBoxSha1Sum The SHA1 hash of a mailto IRI (i.e. the value of an mbox property).
 *                     An LRS MAY include Agents with a matching hash when a request is based on an mbox.
 * @param openId An openID that uniquely identifies the Agent.
 * @param account An openID that uniquely identifies the Agent.
 */
case class Group(name:        Option[String] = None,
                 member:      Option[Seq[Agent]] = None,
                 mBox:        Option[String] = None,
                 mBoxSha1Sum: Option[String] = None,
                 openId:      Option[String] = None, // URI, IRI, IRL figure it out later!!!
                 account:     Option[Account] = None,
                 @deprecated
                 storedId:    Option[Long] = None) extends Actor {

  ActorValidator check this

  override def toString =
    s"""
       |Group instance
       |name        = $name
       |member      = $member
       |mBox        = $mBox
       |mBoxSha1Sum = $mBoxSha1Sum
       |openId      = $openId
       |account     = $account
       |storedId    = $storedId
     """.stripMargin
}


case class Person(var names:    Seq[String],
                  var mboxes:   Seq[String],
                  var mbox_sha1sumes: Seq[String],
                  var openids:  Seq[String],
                  var accounts: Seq[Account]) {

  override def toString =
    s"""
       |Person instance
       |names       = $names
       |mboxes      = $mboxes
       |mbox_sha1sumes = $mbox_sha1sumes
       |openids     = $openids
       |accounts    = $accounts
     """.stripMargin

  def AddAgent(agent: Agent) {
    if (agent.name.isDefined) names = names ++ Seq(agent.name.get)
    if (agent.mBox.isDefined) mboxes = mboxes ++ Seq(agent.mBox.get)
    if (agent.mBoxSha1Sum.isDefined) mbox_sha1sumes = mbox_sha1sumes ++ Seq(agent.mBoxSha1Sum.get)
    if (agent.openId.isDefined) openids = openids ++ Seq(agent.openId.get)
    if (agent.account.isDefined) accounts = accounts ++ Seq(agent.account.get)
  }

}

/**
 * A user account on an existing system e.g. an LMS or intranet.
 * @param homePage The canonical home page for the system the account is on. This is based on FOAF's accountServiceHomePage.
 * @param name The unique id or name used to log in to this account. This is based on FOAF's accountName.
 */
case class Account(homePage: String,
                   name:     String) {

  AccountValidator check this

  override def toString =
    s"""
       |Account instance
       |homePage   = $homePage
       |name       = $name
     """.stripMargin
}

case class AgentProfile(profileId: String,
                        agent:     Agent,
                        content:   Document) {
  override def toString =
    s"""
       |AgentProfile instance
       |profileId   = $profileId
       |agent       = $agent
       |content     = $content
     """.stripMargin
}