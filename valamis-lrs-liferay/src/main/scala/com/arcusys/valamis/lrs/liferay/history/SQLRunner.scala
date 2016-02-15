package com.arcusys.valamis.lrs.liferay.history

import com.arcusys.valamis.lrs.LrsType
import com.arcusys.valamis.lrs.jdbc._
import com.arcusys.valamis.lrs.liferay._
import com.arcusys.valamis.lrs.liferay.util.LiferayDbContext
import com.google.inject.name.Names
import com.google.inject.{Key, Guice}

import scala.slick.driver.JdbcDriver
import scala.slick.jdbc.JdbcBackend

trait SQLRunner extends LrsTypeLocator {

  lazy val lrsType  = LrsType.Simple
  lazy val injector = Guice.createInjector(new LrsModule)
  lazy val driver   = injector.getInstance(classOf[JdbcDriver])
  lazy val db       = injector.getInstance(classOf[JdbcBackend#Database])
  lazy val lrs             = injector.getInstance(Key.get(classOf[JdbcLrs],         Names.named(lrsType.toString)))
  lazy val securityManager = injector.getInstance(Key.get(classOf[SecurityManager], Names.named(lrsType.toString)))
  lazy val valamisReporter = injector.getInstance(Key.get(classOf[ValamisReporter], Names.named(lrsType.toString)))
  lazy val liferayDbContext = injector.getInstance(Key.get(classOf[LiferayDbContext], Names.named(lrsType.toString)))
}
