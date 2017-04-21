package com.arcusys.valamis.lrs.liferay.message

import com.arcusys.valamis.lrs.liferay.util.TokenCleanSupport
import com.google.inject._
import com.liferay.portal.kernel.messaging.{Message, MessageListener}

/**
  *
 * Created by Iliya Tryapitsin on 22.05.15.
 */

@Singleton
class TokenCleanScheduler extends MessageListener with TokenCleanSupport {

  override def receive(message: Message): Unit = {
    cleanExpiredTokens()
  }
}
