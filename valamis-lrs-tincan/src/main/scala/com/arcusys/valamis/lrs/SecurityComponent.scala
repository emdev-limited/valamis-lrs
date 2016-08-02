package com.arcusys.valamis.lrs

import com.arcusys.valamis.lrs.security.{Application, AuthenticationStatus, AuthenticationType, Token}
import com.arcusys.valamis.lrs.tincan._
import org.joda.time.DateTime

/**
  * Security component - actions for security part of LRS
  */
trait SecurityComponent {

  val applicationStorage: ApplicationStorage
  val tokenStorage: TokenStorage

  // One day
  val expiredPeriod = 60 * 60 * 24

  def expiredDateTime = DateTime.now.minusSeconds(expiredPeriod)

  trait ApplicationStorage {

    def getAppScope(appId: String): Option[AuthorizationScope.ValueSet]


    def getApplications(count: Int, offset: Int): Seq[Application]



    def registrationApp(appName: String,
                        appDescription: Option[String],
                        scope: AuthorizationScope.ValueSet,
                        authType: AuthenticationType.Type): Option[Application]

    def getApplication(appId: String): Option[Application]

    def updateApplication(appId: String,
                          name: String,
                          desc: Option[String],
                          scope: AuthorizationScope.ValueSet,
                          authType: AuthenticationType.Type): Unit

    def deleteApplication(appId: String): Unit

    def blockApplication(appId: String): Unit

    def unblockApplication(appId: String): Unit

    def checkByBasic(clientId: String, secret: String): Boolean

  }

  trait TokenStorage {
    def getCallback(token: String): Option[String]

    def checkByToken(appId: String, token: String): Boolean


    def setAuthorized(appId: String,
                      requestCode: String,
                      verified: String): Unit

    def setAccessToken(appId: String,
                       requestCode: String,
                       accessCode: String,
                       accessSecret: String): Unit

    def setRequestToken(appId: String,
                        requestCode: String,
                        requestSecret: String,
                        callback: String): Option[Token]

    def getRequestToken(appId: String,
                        requestCode: String): Option[Token]

    def getAccessToken(appId: String,
                       token: String): Option[Token]

    def clear(): Unit
  }

}