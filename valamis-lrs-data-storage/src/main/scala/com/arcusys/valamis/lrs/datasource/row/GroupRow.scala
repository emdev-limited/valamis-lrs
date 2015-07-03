package com.arcusys.valamis.lrs.datasource.row

import com.arcusys.valamis.lrs.datasource.WithRequireKey

/**
 * Created by Iliya Tryapitsin on 03/01/15.
 */
case class GroupRow(key: StatementObjectRow#Type,
                    name: Option[String],
                    mBox: Option[String],
                    mBoxSha1Sum: Option[String],
                    openId: Option[String],
                    accountKey: AccountRow#KeyType = None) extends ActorRow with WithRequireKey[StatementObjectRow#Type] {
  override def withId[M](e: M) = copy(key = e.asInstanceOf[Type])
}