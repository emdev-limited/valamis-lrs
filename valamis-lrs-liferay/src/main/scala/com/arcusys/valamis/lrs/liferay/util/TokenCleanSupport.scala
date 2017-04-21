package com.arcusys.valamis.lrs.liferay.util

import com.arcusys.valamis.lrs.SecurityManager
import com.arcusys.valamis.lrs.liferay.{Loggable, LrsModule}
import com.google.inject.{Guice, Key}

/**
  * Created by pkornilov on 11.08.16.
  */
trait TokenCleanSupport extends Loggable {

  def cleanExpiredTokens(): Unit = {
    val injector = Guice.createInjector(LrsModule)
    val authentication = injector.getInstance(Key.get(classOf[SecurityManager]))

    logger.info("Start cleaning")

    authentication.clear()
  }

}
