package com.arcusys.valamis.lrs.liferay.history

import com.arcusys.valamis.lrs.DbInit
import com.arcusys.valamis.lrs.liferay.Loggable
import com.arcusys.valamis.lrs.liferay.history.ver230.{DbUpgradeProcess => Ver230}
import com.arcusys.valamis.lrs.liferay.history.ver240.{DbUpgradeProcess => Ver240}
import com.arcusys.valamis.lrs.liferay.history.ver250.{DbUpgradeProcess => Ver250}
import com.arcusys.valamis.lrs.liferay.history.ver270.{DbUpgradeProcess => Ver270}
import com.arcusys.valamis.lrs.liferay.history.ver300.{DbUpgradeProcess => Ver300}
import com.arcusys.valamis.lrs.liferay.history.ver310.{DbUpgradeProcess => Ver310}
import com.google.inject.Key
import com.liferay.portal.kernel.events.SimpleAction

import scala.util.{Failure, Success, Try}

class SetupSchema extends SimpleAction with SQLRunner with Loggable {

  override def run(companyIds: Array[String]): Unit = Try {
    liferayDbContext.setScope(companyIds.head.toLong)

    val schema = injector.getInstance(Key.get(classOf[DbInit]))

    logger.info("Checking Db schema.")

    if (!schema.hasTables) {
      //there are 2 cases to get here:
      // 1. No db schema
      // 2. if liferay upgrade process is finished and schema still is not valid (for example: release record was empty),
      // For both of that cases try to force migration process (that will create schema if needed)
      logger.info("No Db schema or schema is not full. Starting migration.")
      val ver230 = new Ver230
      ver230.doUpgrade()

      val ver240 = new Ver240
      ver240.doUpgrade()

      val ver250 = new Ver250
      ver250.doUpgrade()

      val ver270 = new Ver270
      ver270.doUpgrade()

      val ver300 = new Ver300
      ver300.doUpgrade()

      val ver310 = new Ver310
      ver310.doUpgrade()
    }

  } match {
    case Success(_) => logger.info("Db schema has been upgraded successfully.")
    case Failure(ex) => logger.error(ex)
  }
}