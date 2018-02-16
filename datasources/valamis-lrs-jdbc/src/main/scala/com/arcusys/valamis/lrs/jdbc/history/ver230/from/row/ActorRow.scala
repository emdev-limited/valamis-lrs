package com.arcusys.valamis.lrs.jdbc.history.ver230.from.row

/**
 * Created by Iliya Tryapitsin on 18/03/15.
 */
case class ActorRow(key: Long,
                    id: Option[String],
                    objectType: Option[String],
                    name: Option[String],
                    mBox: Option[String],
                    mBoxSha1Sum: Option[String],
                    openId: Option[String],
                    account: Option[String],
                    memberOf: Option[String])