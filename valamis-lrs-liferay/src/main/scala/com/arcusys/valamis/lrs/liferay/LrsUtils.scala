package com.arcusys.valamis.lrs.liferay

import com.arcusys.valamis.lrs.auth._
import com.arcusys.valamis.lrs.tincan.AuthorizationScope
import com.google.inject.Guice

/**
 * Created by Iliya Tryapitsin on 30.04.15.
 */
object LrsUtils {

  private val module              = new WebServletModule
  private val injector            = Guice.createInjector(module)
  private val authentication = injector.getInstance(classOf[Authentication])

  def registrationApp(appName:        String,
                      appDescription: Option[String],
                      scope:          AuthorizationScope.ValueSet,
                      authType:       AuthenticationType.Type): Option[Application] = {
    authentication.RegistrationApp(appName, appDescription, scope, authType)
  }

  def getRelativeUrl: String = {
    "/valamis-lrs-portlet" + module.lrsUrlPrefix
  }
}
