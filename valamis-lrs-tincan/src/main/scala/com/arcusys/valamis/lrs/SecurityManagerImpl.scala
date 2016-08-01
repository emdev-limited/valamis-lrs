package com.arcusys.valamis.lrs

import com.arcusys.valamis.lrs.security.{Application, AuthenticationStatus, AuthenticationType, Token}
import com.arcusys.valamis.lrs.tincan._

/**
  * Security component - actions for security part of LRS
  */
trait SecurityManagerImpl extends SecurityManager {
  this: SecurityComponent =>

  override def getCallback(token: String): Option[String] = tokenStorage.getCallback(token)

  override def getApplications(count: Int, offset: Int): Seq[Application] = applicationStorage.getApplications(count, offset)

  override def checkByToken(appId: String,
                   token: String,
                   scopeRequest: AuthorizationScope.ValueSet): AuthenticationStatus.Type =
    applicationStorage.getAppScope(appId) match {
      case None => AuthenticationStatus.Denied
      case Some(scope) =>
        val accessRightSuccess = scope <== scopeRequest

        val isExistValidToken = tokenStorage.checkByToken(appId, token)

        if (accessRightSuccess) AuthenticationStatus(isExistValidToken)
        else AuthenticationStatus.Forbidden
    }

  override def checkByBasic(clientId: String,
                   secret: String,
                   scopeRequest: AuthorizationScope.ValueSet): AuthenticationStatus.Type =
    applicationStorage.getAppScope(clientId) match {
      case None => AuthenticationStatus.Denied
      case Some(scope) =>
        val accessRightSuccess = scope <== scopeRequest

        val valid = applicationStorage.checkByBasic(clientId, secret)

        if (accessRightSuccess) AuthenticationStatus(valid)
        else AuthenticationStatus.Forbidden
    }

  override def setAuthorized(appId: String,
                    requestCode: String,
                    verified: String): Unit = tokenStorage.setAuthorized(appId, requestCode, verified)

  override def setAccessToken(appId: String,
                     requestCode: String,
                     accessCode: String,
                     accessSecret: String): Unit = tokenStorage.setAccessToken(appId, requestCode, accessCode, accessSecret)

  override def setRequestToken(appId: String,
                      requestCode: String,
                      requestSecret: String,
                      callback: String): Unit = tokenStorage.setRequestToken(appId, requestCode, requestSecret, callback)

  override def getRequestToken(appId: String,
                      requestCode: String): Option[Token] = tokenStorage.getRequestToken(appId, requestCode)

  override def getAccessToken(appId: String,
                     token: String): Option[Token] = tokenStorage.getAccessToken(appId, token)

  override def registrationApp(appName: String,
                      appDescription: Option[String],
                      scope: AuthorizationScope.ValueSet,
                      authType: AuthenticationType.Type): Option[Application] =
    applicationStorage.registrationApp(appName, appDescription, scope, authType)

  override def getApplication(appId: String): Option[Application] = applicationStorage.getApplication(appId)

  override def updateApplication(appId: String,
                        name: String,
                        desc: Option[String],
                        scope: AuthorizationScope.ValueSet,
                        authType: AuthenticationType.Type): Unit = applicationStorage.updateApplication(appId, name, desc, scope, authType)

  override def deleteApplication(appId: String): Unit = applicationStorage.deleteApplication(appId)

  override def blockApplication(appId: String): Unit = applicationStorage.blockApplication(appId)

  override def unblockApplication(appId: String): Unit = applicationStorage.unblockApplication(appId)

  override def clear(): Unit = tokenStorage.clear()
}