package com.arcusys.valamis.lrs.liferay.test

import com.arcusys.valamis.lrs.auth.AuthModule
import com.arcusys.valamis.lrs.liferay.{AkkaModule, ConfigModule}
import com.arcusys.valamis.lrs.test.config.DbInit
import net.codingwell.scalaguice.ScalaModule

import scala.slick.driver.{JdbcDriver, JdbcProfile}
import scala.slick.jdbc.JdbcBackend

/**
 * Created by Iliya Tryapitsin on 20/03/15.
 */
abstract class BaseCoreModule(val dbInit: DbInit) extends ScalaModule {
  override def configure(): Unit = {
    install(new ConfigModule)
    install(new AkkaModule)
    install(new AuthModule)

    bind[JdbcDriver]  .toInstance(dbInit.driver)
    bind[JdbcProfile] .toInstance(dbInit.driver)
    bind[JdbcBackend#Database].toInstance(dbInit.conn)
    bind[DbInit]      .toInstance(dbInit)
  }
}
