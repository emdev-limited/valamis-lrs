package com.arcusys.valamis.lrs.bundle

import com.liferay.portal.upgrade.registry.UpgradeStepRegistrator
import com.liferay.portal.upgrade.registry.UpgradeStepRegistrator.Registry

import com.arcusys.valamis.lrs.liferay.history.ver230.{DbUpgradeProcess => Ver230}
import com.arcusys.valamis.lrs.liferay.history.ver240.{DbUpgradeProcess => Ver240}
import com.arcusys.valamis.lrs.liferay.history.ver250.{DbUpgradeProcess => Ver250}
import com.arcusys.valamis.lrs.liferay.history.ver270.{DbUpgradeProcess => Ver270}
import com.arcusys.valamis.lrs.liferay.history.ver300.{DbUpgradeProcess => Ver300}
import com.arcusys.valamis.lrs.liferay.history.ver310.{DbUpgradeProcess => Ver310}

/**
  * Created by pkornilov on 09.08.16.
  */
class LrsUpgradeStepRegistrator extends UpgradeStepRegistrator {

  override def register(registry: Registry): Unit = {
    //Liferay Spring Extender will create UpgradeStep from 0 to 200, so our UpgradeSteps starts from 200
    registry.register("com.arcusys.valamis.lrs.bundle","200","230", new Ver230())

    registry.register("com.arcusys.valamis.lrs.bundle","230","240", new Ver240())
    registry.register("com.arcusys.valamis.lrs.bundle","240","250", new Ver250())
    registry.register("com.arcusys.valamis.lrs.bundle","250","270", new Ver270())
    registry.register("com.arcusys.valamis.lrs.bundle","270","300", new Ver300())
    registry.register("com.arcusys.valamis.lrs.bundle","300","310", new Ver310())
  }

}
