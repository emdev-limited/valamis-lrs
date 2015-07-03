package com.arcusys.valamis.lrs.liferay.test

import akka.actor.ActorSystem
import akka.pattern.Patterns
import com.arcusys.valamis.lrs.GuiceAkkaExtension
import com.arcusys.valamis.lrs.auth._
import com.arcusys.valamis.lrs.liferay.WaitPeriod._
import com.arcusys.valamis.lrs.liferay.history.ver230.{DbSchemaUpgrade => Upgrade230}
import com.arcusys.valamis.lrs.liferay.history.ver240.{DbSchemaUpgrade => Upgrade240}
import com.arcusys.valamis.lrs.serializer.StatementSerializer
import com.arcusys.valamis.lrs.services.{StatementQuery, LRS}
import com.arcusys.valamis.lrs.test.config.DbInit
import com.arcusys.valamis.lrs.test.tincan.{Helper, Statements}
import com.arcusys.valamis.lrs.tincan._
import com.arcusys.valamis.lrs._
import com.arcusys.valamis.utils.serialization.JsonHelper
import com.google.inject.Guice
import org.joda.time.DateTime
import org.scalatest._
import org.slf4j.LoggerFactory

import scala.concurrent.Await

/**
 * Created by Iliya Tryapitsin on 20/03/15.
 */
class BaseDataSpec(module: BaseCoreModule)
  extends FeatureSpec
  with BeforeAndAfterAll
  with BeforeAndAfterEach {

  import Helper._

  val logger = LoggerFactory.getLogger("com.arcusys")
  var startDateTime: DateTime = null

  val goodData = Statements.Good.fieldValues
  val injector = Guice.createInjector(module)
  val dbInit   = injector.getInstance(classOf[DbInit])
  val lrs      = injector.getInstance(classOf[LRS])
  val authentication = injector.getInstance(classOf[Authentication])
//  val system   = injector.getInstance(classOf[ActorSystem])
//  val authenticationActor = system.actorOf(GuiceAkkaExtension(system).props(AuthenticationActor.name))

  logger.info(s"Database URL: ${dbInit.toString}")

  val ver230   = new Upgrade230(dbInit.driver, dbInit.conn)
  val ver240   = new Upgrade240(dbInit.driver, dbInit.conn)

  override def beforeEach = startDateTime = DateTime.now()
  override def afterEach  = {
    val testTime = DateTime.now().minus(startDateTime.getMillis).getMillis
    logger.info(s"Test time: ${testTime} msec.")
  }

  override def beforeAll = {
    dbInit.cleanUpBefore

    println(ver230.upgradeMigrations.migrations.mkString(";\n"))
    ver230.upgrade

    println(ver240.upgradeMigrations.migrations.mkString(";\n"))
    ver240.upgrade
  }

  override def afterAll = {
    println(ver240.downgradeMigrations.migrations.mkString(";\n"))
    ver240.downgrade

    println(ver230.downgradeMigrations.migrations.mkString(";\n"))
    ver230.downgrade

    dbInit.cleanUpAfter
  }

  def addStatementTemplate(testCase: String) = scenario(s"Add ${testCase} statement to database") {

    val rawStatement = goodData.get(testCase)
    val json = JsonHelper.toJson(rawStatement)
    val statement = JsonHelper.fromJson[Statement](json, new StatementSerializer)

    lrs.addStatement(statement)
  }

  goodData.foreach { pair => addStatementTemplate(pair._1) }

  scenario("take first 25 statements") {
    val query = StatementQuery(None, None, None, None, None, None, None, None, false, false, 25, 0, FormatType.Exact, false, false)
    val result = lrs.findStatements(query)
    assert(result.seq.size == goodData.size)
  }

  scenario("search verbs with activities") {
    val result = lrs.verbWithActivities("bla".toOption, limit = 2)
    assert(result.count == 1)
    assert(result.seq.size == 1)
  }

  scenario("register application and validate by basic") {

    val regResult = authentication.RegistrationApp("VALAMIS", "VALAMIS".toOption, AuthorizationScope.AllRead, AuthenticationType.OAuth)
    assert(regResult.isDefined)

    val app = regResult.get

    val validBasic = authentication.CheckByBasic(app.appId, app.appSecret, AuthorizationScope.ProfileRead.toValueSet)
    val invalidBasic = authentication.CheckByBasic(app.appId, app.appSecret, AuthorizationScope.All)

    assert(validBasic   == AuthenticationStatus.Allowed)
    assert(invalidBasic == AuthenticationStatus.Denied )
  }
}
