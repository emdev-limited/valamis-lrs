package com.arcusys.valamis.lrs.liferay.history

import com.arcusys.valamis.lrs.liferay.{Loggable, WebServletModule}
import com.google.inject.Guice

import scala.slick.driver.JdbcDriver
import scala.slick.jdbc.JdbcBackend

trait SQLRunner extends Loggable {

  val injector = Guice.createInjector(new WebServletModule)
  val driver   = injector.getInstance(classOf[JdbcDriver])
  val db       = injector.getInstance(classOf[JdbcBackend#Database])
}
