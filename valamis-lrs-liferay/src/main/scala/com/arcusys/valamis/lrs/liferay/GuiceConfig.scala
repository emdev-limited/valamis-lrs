package com.arcusys.valamis.lrs.liferay

import javax.servlet.ServletContextEvent

import com.google.inject.Guice
import com.google.inject.servlet.GuiceServletContextListener

/**
 * Created by Iliya Tryapitsin on 17/01/15.
 */
class GuiceConfig extends GuiceServletContextListener {
  protected def getInjector() = Guice.createInjector(new WebServletModule)

  override def contextDestroyed(ev: ServletContextEvent) = {

    super.contextDestroyed(ev)
  }
}