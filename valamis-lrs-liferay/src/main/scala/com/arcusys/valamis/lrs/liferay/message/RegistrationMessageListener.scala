package com.arcusys.valamis.lrs.liferay.message

import com.arcusys.valamis.lrs.utils._
import com.arcusys.valamis.lrs.liferay._
import com.arcusys.valamis.lrs.liferay.history.SQLRunner
import com.arcusys.valamis.lrs.liferay.Loggable
import com.arcusys.valamis.lrs.security.AuthenticationType
import com.arcusys.valamis.lrs.tincan.AuthorizationScope
import com.arcusys.json.JsonHelper
import com.liferay.portal.kernel.messaging.{Message, MessageBusUtil, MessageListener}

import scala.beans.BeanProperty

class RegistrationMessageListener extends MessageListener with Loggable with SQLRunner {

  @BeanProperty var isOsgiSupport: Boolean = false

  override def receive(message: Message): Unit = {

    liferayDbContext.init()

    logger.info("'Register new app' message received")

    val appName        = message.get("appName"       ).asInstanceOf[String]
    val appDescription = message.get("appDescription").asInstanceOf[String].toOption
    val authScope      = AuthorizationScope.fromString(message.get("authScope").asInstanceOf[String])
    val authType       = AuthenticationType.withName  (message.get("authType" ).asInstanceOf[String])

    val application = securityManager.registrationApp(appName, appDescription, authScope, authType)
    val endpoint    = if (isOsgiSupport) s"/o$liferayUrlPrefix" else liferayUrlPrefix
    val response    = MessageBusUtil.createResponseMessage(message)

    response.setPayload(application match {
      case Some(a) => JsonHelper.toJson(new ResponseModel(a.appId, a.appSecret, endpoint))
      case None    => "None"  //payload must exists in any case
    })

    MessageBusUtil.sendMessage(response.getDestinationName, response)
  }

  case class ResponseModel(appId: String, appSecret: String, endpoint: String)

}
