package com.arcusys.valamis.lrs.liferay.history.ver270

import com.arcusys.valamis.lrs.DbUpgrade
import com.arcusys.valamis.lrs.liferay.UpgradeProcess
import com.arcusys.valamis.lrs.liferay.history.SQLRunner
import com.google.inject.Key
import com.google.inject.name.Names

class DbUpgradeProcess extends UpgradeProcess with SQLRunner {
  override def getThreshold = 270

  val schema = injector.getInstance(Key.get(classOf[DbUpgrade], Names.named("ver270")))

  override def doUpgrade() {
    schema.up(lrs)
  }
}

