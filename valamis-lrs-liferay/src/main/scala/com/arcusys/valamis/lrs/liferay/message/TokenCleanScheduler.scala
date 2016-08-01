package com.arcusys.valamis.lrs.liferay.message

import com.arcusys.valamis.lrs.SecurityManager
import com.arcusys.valamis.lrs.liferay._
import com.google.inject._
import com.liferay.portal.kernel.messaging.{Message, MessageListener}

/**
 * Created by Iliya Tryapitsin on 22.05.15.
 */

@Singleton
class TokenCleanScheduler extends MessageListener with Loggable {

  override def receive(message: Message): Unit = {
    val injector       = Guice.createInjector(LrsModule)
    val authentication = injector.getInstance(Key.get(classOf[SecurityManager]))

    logger.info("Start cleaning")

    authentication.clear()
  }
}
