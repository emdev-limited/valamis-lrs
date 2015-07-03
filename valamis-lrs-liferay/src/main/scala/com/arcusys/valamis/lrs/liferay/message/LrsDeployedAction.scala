package com.arcusys.valamis.lrs.liferay.message

import com.arcusys.valamis.lrs.liferay.Loggable
import com.liferay.portal.kernel.events.SimpleAction
import com.liferay.portal.kernel.messaging.{MessageBusUtil, Message}

/**
 * Created by Iliya Tryapitsin on 23.06.15.
 */
class LrsDeployedAction extends SimpleAction with Loggable {
   override def run(strings: Array[String]): Unit = {
     logger.info("Send startup message")
     val message = new Message()
     message.put("lrs", "deployed")

     MessageBusUtil.sendMessage("valamis/main/lrsDeployed", message)
   }
 }
