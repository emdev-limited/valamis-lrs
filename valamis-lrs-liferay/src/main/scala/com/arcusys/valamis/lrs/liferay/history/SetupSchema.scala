package com.arcusys.valamis.lrs.liferay.history

/**
 * Created by Iliya Tryapitsin on 24.04.15.
 */

import com.arcusys.valamis.lrs.liferay.history.ver230.{DbUpgradeProcess => Ver230}
import com.arcusys.valamis.lrs.liferay.history.ver240.{DbUpgradeProcess => Ver240}
import com.arcusys.valamis.lrs.liferay.history.ver250.{DbUpgradeProcess => Ver250}
import com.liferay.portal.kernel.events.SimpleAction

import scala.util.{Failure, Success, Try}

class SetupSchema extends SimpleAction with SQLRunner {

  override def run(companyIds: Array[String]): Unit = Try {
    liferayDbContext.setScope(companyIds.head.toLong)

    val ver230 = new Ver230
    ver230.doUpgrade()

    val ver240 = new Ver240
    ver240.doUpgrade()

    val ver250 = new Ver250
    ver250.doUpgrade()
  } match {
    case Success(_)  => logger.info("Db schema has been upgraded successfully.")
    case Failure(ex) => logger.error(ex)
  }
}