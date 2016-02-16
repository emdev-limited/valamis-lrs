package features.statementStructure

import com.arcusys.valamis.lrs.api.StatementApi
import com.arcusys.valamis.lrs.test.tincan.{Statements, Helper}
import com.arcusys.json.JsonHelper
import features.BaseFeatureTests
import org.scalatest._
import scala.util.{Failure, Success}

/**
 * Created by Iliya Tryapitsin on 01/04/15.
 */
@Ignore
class GoodStatement extends FeatureSpec with GivenWhenThen with BaseFeatureTests {
  feature("Good statement structure tests") {
//    scenario("mboxAndType agent") {
//      Given("a typical saveStatement request")
//      val statementApi = new StatementApi()
//
//      Given("the statement actor is changed to a mboxAndType agent")
//      val statement = Statements.Good.typical.copy(actor = Agents.mboxAndType)
//      val json = JsonHelper.toJson(statement)
//
//      When("the request is made")
//      statementApi.post(json) match {
//        case Success(code) => Then("the request was successful")
//        case Failure(code) => {
//          Then("the request was failure")
//          fail(s"Response code is $code")
//        }
//      }
//    }
//
//    scenario("mboxSha1AndType agent") {
//      Given("a typical saveStatement request")
//      val statementApi = new StatementApi()
//
//      Given("the statement actor is changed to a mboxSha1AndType agent")
//      val statement = Statements.Good.typical.copy(actor = Agents.mboxSha1AndType)
//      val json = JsonHelper.toJson(statement)
//
//      When("the request is made")
//      statementApi.post(json) match {
//        case Success(code) => Then("the request was successful")
//        case Failure(code) => {
//          Then("the request was failure")
//          fail(s"Response code is $code")
//        }
//      }
//
//    }
//
//    scenario("openidAndType agent") {
//      Given("a typical saveStatement request")
//      val statementApi = new StatementApi()
//
//      Given("the statement actor is changed to a openidAndType agent")
//      val statement = Statements.Good.typical.copy(actor = Agents.openidAndType)
//      val json = JsonHelper.toJson(statement)
//
//      When("the request is made")
//      statementApi.post(json) match {
//        case Success(code) => Then("the request was successful")
//        case Failure(code) => {
//          Then("the request was failure")
//          fail(s"Response code is $code")
//        }
//      }
//    }
//
//    scenario("accountAndType agent") {
//      Given("a typical saveStatement request")
//      val statementApi = new StatementApi()
//
//      Given("the statement actor is changed to a accountAndType agent")
//      val statement = Statements.Good.typical.copy(actor = Agents.accountAndType)
//      val json = JsonHelper.toJson(statement)
//
//      When("the request is made")
//      statementApi.post(json) match {
//        case Success(code) => Then("the request was successful")
//        case Failure(code) => {
//          Then("the request was failure")
//          fail(s"Response code is $code")
//        }
//      }
//    }
//
//    scenario("mboxOnly agent") {
//      Given("a typical saveStatement request")
//      val statementApi = new StatementApi()
//
//      Given("the statement actor is changed to a mboxOnly agent")
//      val statement = Statements.Good.typical.copy(actor = Agents.mboxOnly)
//      val json = JsonHelper.toJson(statement)
//
//      When("the request is made")
//      statementApi.post(json) match {
//        case Success(code) => Then("the request was successful")
//        case Failure(code) => {
//          Then("the request was failure")
//          fail(s"Response code is $code")
//        }
//      }
//    }
//
//    scenario("mboxSha1Only agent") {
//      Given("a typical saveStatement request")
//      val statementApi = new StatementApi()
//
//      Given("the statement actor is changed to a mboxSha1Only agent")
//      val statement = Statements.Good.typical.copy(actor = Agents.mboxSha1Only)
//      val json = JsonHelper.toJson(statement)
//
//      When("the request is made")
//      statementApi.post(json) match {
//        case Success(code) => Then("the request was successful")
//        case Failure(code) => {
//          Then("the request was failure")
//          fail(s"Response code is $code")
//        }
//      }
//    }
//
//    scenario("openidOnly agent") {
//      Given("a typical saveStatement request")
//      val statementApi = new StatementApi()
//
//      Given("the statement actor is changed to a openidOnly agent")
//      val statement = Statements.Good.typical.copy(actor = Agents.openidOnly)
//      val json = JsonHelper.toJson(statement)
//
//      When("the request is made")
//      statementApi.post(json) match {
//        case Success(code) => Then("the request was successful")
//        case Failure(code) => {
//          Then("the request was failure")
//          fail(s"Response code is $code")
//        }
//      }
//    }
//
//    scenario("accountOnly agent") {
//      Given("a typical saveStatement request")
//      val statementApi = new StatementApi()
//
//      Given("the statement actor is changed to a accountOnly agent")
//      val statement = Statements.Good.typical.copy(actor = Agents.accountOnly)
//      val json = JsonHelper.toJson(statement)
//
//      When("the request is made")
//      statementApi.post(json) match {
//        case Success(code) => Then("the request was successful")
//        case Failure(code) => {
//          Then("the request was failure")
//          fail(s"Response code is $code")
//        }
//      }
//    }
//
//    scenario("mboxAndType group") {
//      Given("a typical saveStatement request")
//      val statementApi = new StatementApi()
//
//      Given("the statement actor is changed to a mboxAndType group")
//      val statement = Statements.Good.typical.copy(actor = Groups.mboxAndType)
//      val json = JsonHelper.toJson(statement)
//
//      When("the request is made")
//      statementApi.post(json) match {
//        case Success(code) => Then("the request was successful")
//        case Failure(code) => {
//          Then("the request was failure")
//          fail(s"Response code is $code")
//        }
//      }
//    }
//
//    scenario("mboxSha1AndType group") {
//      Given("a typical saveStatement request")
//      val statementApi = new StatementApi()
//
//      Given("the statement actor is changed to a mboxSha1AndType group")
//      val statement = Statements.Good.typical.copy(actor = Groups.mboxSha1AndType)
//      val json = JsonHelper.toJson(statement)
//
//      When("the request is made")
//      statementApi.post(json) match {
//        case Success(code) => Then("the request was successful")
//        case Failure(code) => {
//          Then("the request was failure")
//          fail(s"Response code is $code")
//        }
//      }
//    }
//
//    scenario("openidAndType group") {
//      Given("a typical saveStatement request")
//      val statementApi = new StatementApi()
//
//      Given("the statement actor is changed to a openidAndType group")
//      val statement = Statements.Good.typical.copy(actor = Groups.openidAndType)
//      val json = JsonHelper.toJson(statement)
//
//      When("the request is made")
//      statementApi.post(json) match {
//        case Success(code) => Then("the request was successful")
//        case Failure(code) => {
//          Then("the request was failure")
//          fail(s"Response code is $code")
//        }
//      }
//    }
//
//    scenario("accountAndType group") {
//      Given("a typical saveStatement request")
//      val statementApi = new StatementApi()
//
//      Given("the statement actor is changed to a accountAndType group")
//      val statement = Statements.Good.typical.copy(actor = Groups.accountAndType)
//      val json = JsonHelper.toJson(statement)
//
//      When("the request is made")
//      statementApi.post(json) match {
//        case Success(code) => Then("the request was successful")
//        case Failure(code) => {
//          Then("the request was failure")
//          fail(s"Response code is $code")
//        }
//      }
//    }
//
//    scenario("mboxAndType group missing objectType") {
//      Given("a typical saveStatement request")
//      val statementApi = new StatementApi()
//
//      Given("the statement actor objectType is removed")
//      val group = Groups.mboxAndType.get.copy(objectType = None)
//
//      Given("the statement actor is changed to a mboxAndType group")
//      val statement = Statements.Good.typical.copy(actor = Some(group))
//      val json = JsonHelper.toJson(statement)
//
//      When("the request is made")
//      statementApi.post(json) match {
//        case Success(code) => Then("the request was successful")
//        case Failure(code) => {
//          Then("the request was failure")
//          fail(s"Response code is $code")
//        }
//      }
//    }

    import Helper._

    val statements = Statements.Good.fieldValues

    def scenarioTemplate(caseName: String) = scenario(s"$caseName statement") {
      Given(s"a $caseName saveStatement request")
      val statementApi = new StatementApi()
      Given(s"get $caseName statement")

      val statement = statements.get(caseName)
      val json = JsonHelper.toJson(statement)

      When("the request is made")
      statementApi.post(json) match {
        case Success(code) => Then("the request was successful")
        case Failure(code) => {
          Then("the request was failure")
          fail(s"Response code is ${code.getMessage}")
        }
      }
    }

    statements foreach { x => scenarioTemplate(x._1) }

  }

}
