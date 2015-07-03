package com.arcusys.valamis.lrs.liferay.message

import com.arcusys.valamis.lrs._
import com.arcusys.valamis.lrs.auth.AuthenticationType
import com.arcusys.valamis.lrs.liferay.{Loggable, LrsUtils}
import com.arcusys.valamis.lrs.tincan.AuthorizationScope
import com.arcusys.valamis.utils.serialization.JsonHelper
import com.liferay.portal.kernel.messaging.{Message, MessageBusUtil, MessageListener}

class RegistrationMessageListener extends MessageListener with Loggable {

  override def receive(message: Message): Unit = {
    logger.info("'Register new app' message received")

    val appName        = message.get("appName"       ).asInstanceOf[String]
    val appDescription = message.get("appDescription").asInstanceOf[String].toOption
    val authScope      = AuthorizationScope.fromString(message.get("authScope").asInstanceOf[String])
    val authType       = AuthenticationType.withName  (message.get("authType" ).asInstanceOf[String])

    val application = LrsUtils.registrationApp(appName, appDescription, authScope, authType)
    val endpoint    = LrsUtils.getRelativeUrl
    val response    = MessageBusUtil.createResponseMessage(message)

    response.setPayload(application match {
      case Some(a) => JsonHelper.toJson(new ResponseModel(a.appId, a.appSecret, endpoint))
      case None    => "None"  //payload must exists in any case
    })

    MessageBusUtil.sendMessage(response.getDestinationName, response)
  }

  case class ResponseModel(appId: String, appSecret: String, endpoint: String)

}
