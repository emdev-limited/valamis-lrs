package com.arcusys.valamis.lrs.jdbc.database.api.query

import com.arcusys.valamis.lrs.jdbc.database.LrsDataContext
import com.arcusys.valamis.lrs.jdbc.database.row.ActorRow

/**
 * Created by Iliya Tryapitsin on 08.07.15.
 */
trait ActorQueries extends TypeAliases {
  this: LrsDataContext =>

  import executionContext.driver.simple._

  private type MBoxCol        = ConstColumn[Option[String]]
  private type MBoxSha1SumCol = ConstColumn[Option[String]]
  private type OpenIdCol      = ConstColumn[Option[String]]
  private type NameCol        = ConstColumn[String]
  private type KeyCol         = ConstColumn[ActorRow#Type]
  private type KeysCol        = Seq[ActorRow#Type]
  private type HomepageCol    = ConstColumn[String]

  private def findGroupMembersQ (groupKey: KeyCol) =
    actors filter {
      x => x.groupKey === groupKey
    } leftJoin accounts on {
      (x1, x2) => x1.accountKey === x2.key

    } map { case (x1, x2) => (x1, x2.?) }

  def findGroupMembersByKeysQ (groupKeys: Seq[ActorRow#Type]) =
    actors filter {
      x => x.groupKey inSet groupKeys
    } leftJoin accounts on {
      (x1, x2) => x1.accountKey === x2.key

    } map { case (x1, x2) => (x1, x2.?) }


  private def findActorByKeyQ (key: KeyCol) =
    actors filter {
      x => x.key === key

    } leftJoin accounts on {
      (x1, x2) => x1.accountKey === x2.key

    } map { case (x1, x2) => (x1, x2.?) }


  def findActorsByKeysQ (keys: Seq[ActorRow#Type]) =
    actors filter {
      x => x.key inSet keys

    } leftJoin accounts on {
      (x1, x2) => x1.accountKey === x2.key

    } map { case (x1, x2) => (x1, x2.?) }

  /**
   * Search actors by mbox, mbox sha1 sum, openId or name and home page
   * @param mbox E-mail
   * @param sha1SumCol Hash of e-mail
   * @param openIdCol Open id account url
   * @param nameCol User name
   * @param homepageCol Home page
   * @return Actors storage query
   */
  def findActorByAccountQ (mbox:        MBoxCol,
                           sha1SumCol:  MBoxSha1SumCol,
                           openIdCol:   OpenIdCol,
                           nameCol:     NameCol,
                           homepageCol: HomepageCol) =
    actors leftJoin accounts on { (actor, account) =>
      actor.accountKey === account.key

    } filter { case (actorRec, accountRec) =>
      actorRec.mBox        === mbox        ||
      actorRec.mBoxSha1Sum === sha1SumCol  ||
      actorRec.openId      === openIdCol   ||
      accountRec.name      === nameCol     &&
      accountRec.homepage  === homepageCol

    } map { x =>
      x._1
    }

  /**
   * Search actors by mbox, mbox sha1 sum or openId
   * @param mbox E-mail
   * @param sha1SumCol Hash of e-mail
   * @param openIdCol Open id account url
   * @return Actors storage query
   */
  def findActorQ (mbox:        MBoxCol,
                  sha1SumCol:  MBoxSha1SumCol,
                  openIdCol:   OpenIdCol) =
    actors leftJoin accounts on { (actor, account) =>
      actor.accountKey === account.key

    } filter { case (actorRec, accountRec) =>
      actorRec.mBox        === mbox        ||
      actorRec.mBoxSha1Sum === sha1SumCol  ||
      actorRec.openId      === openIdCol

    } map { x =>
      x._1
    }

//  /**
//   * Search actors by mbox, mbox sha1 sum or openId
//   * @param mbox E-mail
//   * @param sha1SumCol Hash of e-mail
//   * @param openIdCol Open id account url
//   * @return Actors storage query
//   */
//  def findActorKeysQ (mbox:        Seq[String],
//                      sha1SumCol:  Seq[String],
//                      openIdCol:   Seq[String]) =
//    actors leftJoin accounts on { (actor, account) =>
//      actor.accountKey === account.key
//
//    } filter { case (actorRec, accountRec) =>
//      actorRec.mBox        inSetBind mbox       ||
//      actorRec.mBoxSha1Sum inSetBind sha1SumCol ||
//      actorRec.openId      inSetBind openIdCol
//
//    } map { x =>
//      x._1
//    }

  /**
   * Search actors by mbox, mbox sha1 sum,
   * openId or name and home page compiled query return only keys
   */
  val findActorKeyWithAccountQC = Compiled(
    (mbox:        MBoxCol,
     sha1SumCol:  MBoxSha1SumCol,
     openIdCol:   OpenIdCol,
     nameCol:     NameCol,
     homepageCol: HomepageCol) =>

    findActorByAccountQ(mbox, sha1SumCol, openIdCol, nameCol, homepageCol) map {
      x => x.key
    })

  /**
   * Search actors by mbox, mbox sha1 sum or compiled query return only keys
   */
  val findActorKeyQC = Compiled(
    (mbox:        MBoxCol,
     sha1SumCol:  MBoxSha1SumCol,
     openIdCol:   OpenIdCol) =>

    findActorQ(mbox, sha1SumCol, openIdCol) map {
      x => x.key
    })

  val findActorByKeyQC   = Compiled((key: KeyCol)      => findActorByKeyQ(key))
  val findGroupMembersQC = Compiled((groupKey: KeyCol) => findGroupMembersQ(groupKey))
}
