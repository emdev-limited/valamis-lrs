package com.arcusys.valamis.lrs.liferay.history.ver230

import com.arcusys.valamis.lrs.DbUpgrade
import com.arcusys.valamis.lrs.liferay.history.SQLRunner
import com.arcusys.valamis.lrs.liferay.UpgradeProcess
import com.google.inject.Key
import com.google.inject.name.Names

class DbUpgradeProcess extends UpgradeProcess with SQLRunner {
  override def getThreshold = 230
  val schema = injector.getInstance(Key.get(classOf[DbUpgrade], Names.named("ver230")))

  override def doUpgrade() {
    schema.up(lrs)
  }
}