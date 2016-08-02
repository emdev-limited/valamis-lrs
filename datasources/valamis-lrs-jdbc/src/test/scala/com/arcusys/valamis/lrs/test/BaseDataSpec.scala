package com.arcusys.valamis.lrs.test

import com.arcusys.json.JsonHelper
import com.arcusys.valamis.lrs._
import com.arcusys.valamis.lrs.security.{AuthenticationStatus, AuthenticationType}
import com.arcusys.valamis.lrs.serializer.StatementSerializer
import com.arcusys.valamis.lrs.test.tincan.{Helper, Statements}
import com.arcusys.valamis.lrs.tincan._
import org.scalatest._

/**
 * Created by Iliya Tryapitsin on 20/03/15.
 */
class BaseDataSpec(module: BaseCoreModule)
  extends BaseDatabaseSpec(module)
  with FeatureSpecLike
  with BeforeAndAfterAll
  with BeforeAndAfterEach {

  import Helper._

  val goodData = Statements.Good.fieldValues

  logger.info(s"Database URL: ${dbInit.toString}")

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
    val result = valamisReporter.verbWithActivities("bla" ?, limit = 2)
    assert(result.count == 0)
    assert(result.seq.size == 0)
  }

  scenario("register application and validate by basic") {

    val regResult = securityManager.registrationApp("VALAMIS", "VALAMIS".toOption, AuthorizationScope.AllRead, AuthenticationType.OAuth)
    assert(regResult.isDefined)

    val app = regResult.get

    val validBasic = securityManager.checkByBasic(app.appId, app.appSecret, AuthorizationScope.ProfileRead.toValueSet)
    val invalidBasic = securityManager.checkByBasic(app.appId, app.appSecret, AuthorizationScope.All)

    assert(validBasic   == AuthenticationStatus.Allowed)
    assert(invalidBasic == AuthenticationStatus.Forbidden )
  }
}
