package com.arcusys.valamis.lrs.liferay.message

import com.arcusys.valamis.lrs.auth.Authentication
import com.arcusys.valamis.lrs.liferay.{Loggable, WebServletModule}
import com.google.inject._
import com.liferay.portal.kernel.messaging.{Message, MessageListener}

/**
 * Created by Iliya Tryapitsin on 22.05.15.
 */

@Singleton
class TokenCleanScheduler extends MessageListener with Loggable {

  override def receive(message: Message): Unit = {
    val injector       = Guice.createInjector(new WebServletModule)
    val authentication = injector.getInstance(classOf[Authentication])

    logger.info("Start cleaning")

    authentication.Clear()
  }
}
