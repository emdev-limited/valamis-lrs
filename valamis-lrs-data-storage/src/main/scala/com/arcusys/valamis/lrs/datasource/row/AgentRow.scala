package com.arcusys.valamis.lrs.datasource.row

import com.arcusys.valamis.lrs.datasource.WithRequireKey

/**
 * Created by Iliya Tryapitsin on 03/01/15.
 */
case class AgentRow(key: StatementObjectRow#Type,
                    name: Option[String] = None,
                    mBox: Option[String] = None,
                    mBoxSha1Sum: Option[String] = None,
                    openId: Option[String] = None,
                    accountKey: AccountRow#KeyType = None,
                    groupKey: Option[GroupRow#Type] = None) extends ActorRow with WithRequireKey[StatementObjectRow#Type] {
  override def withId[M](e: M) = copy(key = e.asInstanceOf[Type])
}