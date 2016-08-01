package com.arcusys.valamis.lrs.liferay.history

import com.arcusys.valamis.lrs.liferay._
import com.arcusys.valamis.lrs.liferay.util.DbContext
import com.arcusys.valamis.lrs.{Lrs, SecurityManager, ValamisReporter}
import com.google.inject.{Guice, Key}

trait SQLRunner {

  lazy val injector = Guice.createInjector(LrsModule)
  lazy val lrs             = injector.getInstance(Key.get(classOf[Lrs]))
  lazy val securityManager = injector.getInstance(Key.get(classOf[SecurityManager]))
  lazy val valamisReporter = injector.getInstance(Key.get(classOf[ValamisReporter]))
  lazy val liferayDbContext = injector.getInstance(Key.get(classOf[DbContext]))
}
