package com.arcusys.valamis.lrs.jdbc.database.api

import com.arcusys.valamis.lrs._
import com.arcusys.valamis.lrs.jdbc.database.LrsDataContext
import com.arcusys.valamis.lrs.jdbc.database.api.query.ActorQueries
import com.arcusys.valamis.lrs.jdbc.database.row._
import com.arcusys.valamis.lrs.tincan._

/**
 * Created by Iliya Tryapitsin on 08.07.15.
 */
trait ActorApi extends ActorQueries {
  this: LrsDataContext
    with StatementObjectApi
    with AccountApi =>

  import executionContext.driver.simple._

  /**
   * Load TinCan [[Group]] by storage key
   * @param key Storage key
   * @return Tincan [[Group]]
   */
  def findGroupByKey (key: ActorRow#Type)
                     (implicit s: Session): Group = {
    val (actorRec, accountRec) = findActorByKeyQC (key) first

    actorRec.convert withAccount {
      accountRec map { x => x.convert }
    } withMembers {
      getGroupMembers(key)
    } buildGroup
  }

  /**
   * Load TinCan [[Group]] members by storage key
   * @param key Group Storage key
   * @return List of Tincan [[Agent]]
   */
  private def getGroupMembers (key: ActorRow#Type)
                              (implicit s: Session) =
    findGroupMembersQC(key).run map {
      case (agentRec, accountRec) => agentRec.convert withAccount {
        accountRec map { x => x.convert }
      } buildAgent
    }

  /**
   * Load TinCan [[Agent]] by storage key
   * @param key Storage key
   * @return Tincan [[Agent]]
   */
  def findAgentByKey (key: ActorRow#Type)
                     (implicit s: Session): Agent = {
    val (actorRec, accountRec) = findActorByKeyQC (key) first

    actorRec.convert withAccount {
      accountRec map { x => x.convert }
    } buildAgent
  }

  /**
   * Load TinCan [[Actor]] by storage key
   * @param key Storage key
   * @return Tincan [[Actor]]
   */
  def findActorByKey (key: ActorRow#Type)
                     (implicit s: Session): Actor = {
    val (actorRec, accountRec) = findActorByKeyQC (key) first

    actorRec match {
      case a: AgentRow =>
        a.convert withAccount {
          accountRec map { x => x.convert }
        } buildAgent

      case g: GroupRow =>
        g.convert withMembers {
          getGroupMembers(key)
        } withAccount {
          accountRec map { x => x.convert }
        } buildGroup
    }
  }

  /**
   * Load Tincan [[Group]]s by storage keys
   * @param keys Storage keys
   * @param s
   * @return Tincan [[Group]]s with keys
   */
  def findGroupsByKeys (keys: Seq[ActorRow#Type])
                       (implicit s: Session): Map[GroupRow#Type, Group] =
    findActorsByKeys (keys) filter {
      case (v, k) => k.isInstanceOf[Group]
    } map {
      case (v, k: Group) => (v, k.asInstanceOf[Group])
    }

  /**
   * Load TinCan [[Actor]]s by storage key
   * @param keys Storage keys
   * @return Tincan [[Actor]] list with storage keys
   */
  def findActorsByKeys (keys: Seq[ActorRow#Type])
                       (implicit s: Session): Map[ActorRow#Type, Actor] =
    if (keys isEmpty) Map()
    else {

      // Load all group members key selected keys and convert to Tincan Agent instances
      val members = findGroupMembersByKeysQ (keys).run map {
        case (agentRec: AgentRow, accountRec) =>
          val agent = agentRec.convert withAccount {
            accountRec map { x => x.convert }
          } buildAgent

          (agentRec.groupKey get, agent)
      }

      findActorsByKeysQ (keys).run map { case (actorRec, accountRec) =>

        val actor = actorRec match {
          case a: AgentRow =>
            a.convert withAccount {
              accountRec map { x => x.convert }
            } buildAgent

          case g: GroupRow =>
            g.convert withMembers {
              // Find members in a heap
              members filter { case (k, v) => k == g.key } map { case (k, v) => v }
            } withAccount {
              accountRec map { x => x.convert }
            } buildGroup
        }

        (actorRec.key, actor)
      } toMap
    }

  implicit class ActorInsertQueries (q: ActorQ) {

    def keyForQ (actor: Actor) = actor.account match {
      case Some(account) =>
        findActorByAccountQ(
          actor.mBox,
          actor.mBoxSha1Sum,
          actor.openId,
          account.name,
          account.homePage) map { _.key }

      case None =>
        findActorQ(
          actor.mBox,
          actor.mBoxSha1Sum,
          actor.openId) map { _.key }
    }

    def keysForQ (a: Seq[Actor]) =
      actors leftJoin accounts on { (actor, account) =>
        actor.accountKey === account.key

      } filter { case (actorRec, accountRec) =>
        (actorRec.mBox        inSetBind a.map(_.mBox       ).takeDefined) ||
        (actorRec.mBoxSha1Sum inSetBind a.map(_.mBoxSha1Sum).takeDefined) ||
        (actorRec.openId      inSetBind a.map(_.openId     ).takeDefined)

      } map { x =>
        x._1.key
      }

    /**
     * Find storage key for Tincan [[Actor]]
     * @param actor Tincan [[Actor]]
     * @return Storage key
     */
    def keyFor (actor: Actor)
               (implicit session: Session)= actor.account match {
      case Some(account) =>
        findActorKeyWithAccountQC((
          actor.mBox,
          actor.mBoxSha1Sum,
          actor.openId,
          account.name,
          account.homePage)) firstOption

      case None =>
        findActorKeyQC((
          actor.mBox,
          actor.mBoxSha1Sum,
          actor.openId)) firstOption
    }

    /**
     * Insert unique Tincan Actor to the storage
     * @param actor Actor instance
     * @param session
     * @return Identity key in the storage
     */
    def addUnique (actor: Actor)
                  (implicit session: Session): ActorRow#Type =
      keyFor(actor) getOrElse {
        q add actor
      }

    /**
     * Insert unique Tincan Actor to the storage
     * @param actor Actor instance
     * @param session
     * @return Identity key in the storage
     */
    def addUnique (actor: Option[Actor])
                  (implicit session: Session): Option[ActorRow#Type] =
      actor map { x => q add x }

    /**
     * Insert Tincan Actor to the storage
     * @param actor Actor instance
     * @param session
     * @return Identity key in the storage
     */
    def add (actor: Actor)
            (implicit session: Session): ActorRow#Type =
      actor match {
        case a: Agent => q add a
        case g: Group => q add g
      }

    /**
     * Insert Tincan Actor to the storage
     * @param actor Actor instance
     * @param session
     * @return Identity key in the storage
     */
    def add (actor: Option[Actor])
            (implicit session: Session): Option[ActorRow#Type] =
      actor whenDefined { x => q add x }

    /**
     * Insert Tincan Agent to the storage
     * @param agent Agent instance
     * @param session
     * @return Identity key in the storage
     */
    def add (agent: Agent)
            (implicit session: Session): ActorRow#Type =
      agent.convert withKey {
        statementObjects add agent

      } withAccount {
        accounts addUnique agent.account

      } build { r =>
        q += r
      }

    /**
     * Insert Tincan Agent to the storage
     * @param rec Agent instance with Group identity key
     * @param session
     * @return Identity key in the storage
     */
    def add (rec: (Agent, ActorRow#Type))
            (implicit session: Session): ActorRow#Type =
      keyFor(rec._1) getOrElse {
        rec._1.convert withKey {
          statementObjects add rec._1

        } withAccount {
          accounts add rec._1.account

        } withGroup { rec._2 } build { r =>
          q += r
        }
      }

    /**
     * Insert unique Tincan Actor to the storage
     * @param recs Actor instance list with Group identity key
     * @param session
     * @return Identity keys in the storage
     */
    def addSeq (recs: (Seq[Agent], ActorRow#Type))
               (implicit session: Session): Seq[ActorRow#Type] =
      recs._1 map { x => actors add (x, recs._2) }

    /**
     * Insert Tincan Group to the storage
     * @param group Group instance
     * @param session
     * @return Identity key in the storage
     */
    def add (group: Group)
            (implicit session: Session): ActorRow#Type =
      group.convert withKey {
        statementObjects add group

      } withAccount {
        accounts add group.account

      } build { r =>
        q += r

        group.member map { x => actors addSeq (x, r.key) }
      }
  }
}