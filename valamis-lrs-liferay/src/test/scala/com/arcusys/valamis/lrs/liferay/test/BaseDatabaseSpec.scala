package com.arcusys.valamis.lrs.liferay.test

import com.arcusys.valamis.lrs.liferay.history.ver230.{DbSchemaUpgrade => Upgrade230}
import com.arcusys.valamis.lrs.liferay.history.ver240.{DbSchemaUpgrade => Upgrade240}
import com.arcusys.valamis.lrs.liferay.history.ver250.{DbSchemaUpgrade => Upgrade250}
import com.arcusys.valamis.lrs.jdbc._
import com.arcusys.valamis.lrs.test.config.DbInit
import com.arcusys.valamis.lrs.test.tincan._
import com.arcusys.valamis.lrs._
import com.google.inject.name.Names
import com.google.inject.{Key, Guice}
import org.joda.time.DateTime
import org.scalatest._
import org.slf4j.LoggerFactory


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
  val executionContext = injector.getInstance(Key.get(classOf[ExecutionContext], Names.named(LrsType.SimpleName)))
  val lrs      = injector.getInstance(Key.get(classOf[JdbcLrs], Names.named(LrsType.SimpleName)))
  val valamisReporter = injector.getInstance(Key.get(classOf[ValamisReporter], Names.named(LrsType.SimpleName)))
  val securityManager = injector.getInstance(Key.get(classOf[SecurityManager], Names.named(LrsType.SimpleName)))
  val ver230   = new Upgrade230(dbInit.driver, dbInit.conn, lrs)
  val ver240   = new Upgrade240(dbInit.driver, dbInit.conn, lrs)
  val ver250   = new Upgrade250(dbInit.driver, dbInit.conn)


  override def beforeEach = startDateTime = DateTime.now()
  override def afterEach  =  {
    DateTime.now()
      .minus(startDateTime.getMillis)
      .getMillis
      .then { ms => logger.info(s"Test time: ${ms} msec.") }
  }

  override def beforeAll = {
    dbInit.cleanUpBefore

    logger.debug(ver230.upgradeMigrations.migrations.mkString(";\n"))
    ver230.upgrade

    logger.debug(ver240.upgradeMigrations.migrations.mkString(";\n"))
    ver240.upgrade

    logger.debug(ver250.upgradeMigrations.migrations.mkString(";\n"))
    ver250.upgrade
  }

  override def afterAll = {
    logger.debug(ver240.downgradeMigrations.migrations.mkString(";\n"))
    ver240.downgrade

    logger.debug(ver230.downgradeMigrations.migrations.mkString(";\n"))
    ver230.downgrade

    dbInit.cleanUpAfter
  }
}
