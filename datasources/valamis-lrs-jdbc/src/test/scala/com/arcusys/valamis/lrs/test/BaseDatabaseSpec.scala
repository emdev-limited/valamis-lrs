package com.arcusys.valamis.lrs.test

import com.arcusys.valamis.lrs._
import com.arcusys.valamis.lrs.history.BaseDbUpgrade
import com.arcusys.valamis.lrs.history.ver230.{DbSchemaUpgrade => Upgrade230}
import com.arcusys.valamis.lrs.history.ver240.{DbSchemaUpgrade => Upgrade240}
import com.arcusys.valamis.lrs.history.ver250.{DbSchemaUpgrade => Upgrade250}
import com.arcusys.valamis.lrs.jdbc.JdbcLrs
import com.arcusys.valamis.lrs.test.config.DbInit
import com.arcusys.valamis.lrs.test.tincan._
import com.google.inject.name.Names
import com.google.inject.{Guice, Key}
import org.joda.time.DateTime
import org.scalatest._
import org.slf4j.LoggerFactory

import scala.slick.driver.JdbcDriver
import scala.slick.jdbc.JdbcBackend


/**
 * Created by Iliya Tryapitsin on 25.06.15.
 */
abstract class BaseDatabaseSpec(module: BaseCoreModule)
  extends Suite
  with BeforeAndAfterAll
  with BeforeAndAfterEach {

  import Helper._

  val agents     = Agents    .Good.fieldValues
  val scores     = Scores    .Good.fieldValues
  val results    = Results   .Good.fieldValues
  val statements = Statements.Good.fieldValues
  val activities = Activities.Good.fieldValues
  val contexts   = Contexts  .Good.fieldValues

  var startDateTime: DateTime = null
  val logger   = LoggerFactory.getLogger("com.arcusys")
  val injector = Guice.createInjector(module)
  val dbInit   = injector.getInstance(classOf[DbInit])
  val db   = injector.getInstance(classOf[JdbcBackend#Database])
  val driver   = injector.getInstance(classOf[JdbcDriver])
  val lrs      = injector.getInstance(Key.get(classOf[Lrs])).asInstanceOf[JdbcLrs]
  val valamisReporter = injector.getInstance(Key.get(classOf[ValamisReporter]))
  val securityManager = injector.getInstance(Key.get(classOf[SecurityManager]))
  val ver230   = injector.getInstance(Key.get(classOf[BaseDbUpgrade], Names.named("ver230")))
  val ver240   = injector.getInstance(Key.get(classOf[BaseDbUpgrade], Names.named("ver240")))
  val ver250   = injector.getInstance(Key.get(classOf[BaseDbUpgrade], Names.named("ver250")))
  val ver300   = injector.getInstance(Key.get(classOf[BaseDbUpgrade], Names.named("ver300")))


  override def beforeEach = startDateTime = DateTime.now()
  override def afterEach  =  {
    DateTime.now()
      .minus(startDateTime.getMillis)
      .getMillis
      .afterThat { ms => logger.info(s"Test time: ${ms} msec.") }
  }

  override def beforeAll = {
    dbInit.cleanUpBefore

    logger.debug(ver230.upgradeMigrations.migrations.mkString(";\n"))
    ver230.upgrade

    logger.debug(ver240.upgradeMigrations.migrations.mkString(";\n"))
    ver240.upgrade

    logger.debug(ver250.upgradeMigrations.migrations.mkString(";\n"))
    ver250.upgrade

    logger.debug(ver300.upgradeMigrations.migrations.mkString(";\n"))
    ver300.upgrade


  }

  override def afterAll = {
    logger.debug(ver240.downgradeMigrations.migrations.mkString(";\n"))
    ver240.downgrade

    logger.debug(ver230.downgradeMigrations.migrations.mkString(";\n"))
    ver230.downgrade

    dbInit.cleanUpAfter
  }
}
