package features.statementStructure

import com.arcusys.valamis.lrs.api.StatementApi
import com.arcusys.valamis.lrs.test.tincan.{Statements, Helper}
import com.arcusys.json.JsonHelper
import features.BaseFeatureTests
import org.scalatest._

import scala.util.{Failure, Success}

/**
 * Created by Iliya Tryapitsin on 26/02/15.
 */
@Ignore
class BadStatement extends FeatureSpec with GivenWhenThen with BaseFeatureTests {
  feature("Bad statement structure tests") {

//    scenario("Bad actor: mboxAndType agent with bad mbox 'conformancetest@tincanapi.com'") {
//      Given("a typical saveStatement request")
//      val statementApi = new StatementApi()
//
//      Given("the statement actor mbox is changed to 'conformancetest@tincanapi.com'")
//      val agent = Agents.mboxAndType.get.copy(mbox = Some("conformancetest@tincanapi.com"))
//
//      Given("the statement actor is changed to a mboxAndType agent")
//      val statement = Statements.Good.typical.copy(actor = Some(agent))
//      val json = JsonHelper.toJson(statement)
//
//      When("the request is made")
//      statementApi.post(json) match {
//        case Success(code) => {
//          Then("the request was successful")
//          fail(s"Response code is $code")
//        }
//        case Failure(code) => Then("the LRS responds with HTTP 400")
//      }
//    }
//
//    scenario("Bad actor: mboxAndType agent with bad mbox 'bad mbox'") {
//      Given("a typical saveStatement request")
//      val statementApi = new StatementApi()
//
//      Given("the statement actor mbox is changed to 'bad mbox'")
//      val agent = Agents.mboxAndType.get.copy(mbox = Some("bad mbox"))
//
//      Given("the statement actor is changed to a mboxAndType agent")
//      val statement = Statements.Good.typical.copy(actor = Some(agent))
//      val json = JsonHelper.toJson(statement)
//
//      When("the request is made")
//      statementApi.post(json) match {
//        case Success(code) => {
//          Then("the request was successful")
//          fail(s"Response code is $code")
//        }
//        case Failure(code) => Then("the LRS responds with HTTP 400")
//      }
//    }
//
//    scenario("Bad actor: mboxAndType agent with bad objectType 'notAgent'") {
//      Given("a typical saveStatement request")
//      val statementApi = new StatementApi()
//
//      Given("the statement actor objectType is changed to 'notAgent'")
//      val agent = Agents.mboxAndType.get.copy(objectType = Some("notAgent"))
//
//      Given("the statement actor is changed to a mboxAndType agent")
//      val statement = Statements.Good.typical.copy(actor = Some(agent))
//      val json = JsonHelper.toJson(statement)
//
//      When("the request is made")
//      statementApi.post(json) match {
//        case Success(code) => {
//          Then("the request was successful")
//          fail(s"Response code is $code")
//        }
//        case Failure(code) => Then("the LRS responds with HTTP 400")
//      }
//    }
//
//    scenario("Bad actor: mboxAndType group with bad mbox 'conformancetest@tincanapi.com'") {
//      Given("a typical saveStatement request")
//      val statementApi = new StatementApi()
//
//      Given("the statement actor mbox is changed to 'conformancetest@tincanapi.com'")
//      val group = Groups.mboxAndType.get.copy(mbox = Some("conformancetest@tincanapi.com"))
//
//      Given("the statement actor is changed to a mboxAndType group")
//      val statement = Statements.Good.typical.copy(actor = Some(group))
//      val json = JsonHelper.toJson(statement)
//
//      When("the request is made")
//      statementApi.post(json) match {
//        case Success(code) => {
//          Then("the request was successful")
//          fail(s"Response code is $code")
//        }
//        case Failure(code) => Then("the LRS responds with HTTP 400")
//      }
//    }
//
//    scenario("Bad actor: mboxAndType group with bad mbox 'bad mbox'") {
//      Given("a typical saveStatement request")
//      val statementApi = new StatementApi()
//
//      Given("the statement actor mbox is changed to 'bad mbox'")
//      val group = Groups.mboxAndType.get.copy(mbox = Some("bad mbox"))
//
//      Given("the statement actor is changed to a mboxAndType group")
//      val statement = Statements.Good.typical.copy(actor = Some(group))
//      val json = JsonHelper.toJson(statement)
//
//      When("the request is made")
//      statementApi.post(json) match {
//        case Success(code) => {
//          Then("the request was successful")
//          fail(s"Response code is $code")
//        }
//        case Failure(code) => Then("the LRS responds with HTTP 400")
//      }
//    }
//
//    scenario("Bad actor: mboxAndType group with bad objectType 'notGroup'") {
//      Given("a typical saveStatement request")
//      val statementApi = new StatementApi()
//
//      Given("the statement actor objectType is changed to 'notGroup'")
//      val group = Groups.mboxAndType.get.copy(objectType = Some("notAgent"))
//
//      Given("the statement actor is changed to a mboxAndType group")
//      val statement = Statements.Good.typical.copy(actor = Some(group))
//      val json = JsonHelper.toJson(statement)
//
//      When("the request is made")
//      statementApi.post(json) match {
//        case Success(code) => {
//          Then("the request was successful")
//          fail(s"Response code is $code")
//        }
//        case Failure(code) => Then("the LRS responds with HTTP 400")
//      }
//    }
//
//    scenario("Bad actor: allPropertiesMboxAgentMember group with mbox 'conformancetest@tincanapi.com'") {
//      Given("a typical saveStatement request")
//      val statementApi = new StatementApi()
//
//      Given("the statement actor mbox is changed to 'conformancetest@tincanapi.com'")
//      val group = Groups.allPropertiesMboxAgentMember.get.copy(mbox = Some("conformancetest@tincanapi.com"))
//
//      Given("the statement actor is changed to a allPropertiesMboxAgentMember group")
//      val statement = Statements.Good.typical.copy(actor = Some(group))
//      val json = JsonHelper.toJson(statement)
//
//      When("the request is made")
//      statementApi.post(json) match {
//        case Success(code) => {
//          Then("the request was successful")
//          fail(s"Response code is $code")
//        }
//        case Failure(code) => Then("the LRS responds with HTTP 400")
//      }
//    }
//
//    scenario("Bad actor: allPropertiesMboxAgentMember group with mbox 'bad mbox'") {
//      Given("a typical saveStatement request")
//      val statementApi = new StatementApi()
//
//      Given("the statement actor mbox is changed to 'bad mbox'")
//      val group = Groups.allPropertiesMboxAgentMember.get.copy(mbox = Some("bad mbox"))
//
//      Given("the statement actor is changed to a allPropertiesMboxAgentMember group")
//      val statement = Statements.Good.typical.copy(actor = Some(group))
//      val json = JsonHelper.toJson(statement)
//
//      When("the request is made")
//      statementApi.post(json) match {
//        case Success(code) => {
//          Then("the request was successful")
//          fail(s"Response code is $code")
//        }
//        case Failure(code) => Then("the LRS responds with HTTP 400")
//      }
//    }

    import Helper._

    val statements = Statements.Bad.fieldValues

    def scenarioTemplate(caseName: String) = scenario(s"$caseName statement") {
      Given(s"a $caseName saveStatement request")
      val statementApi = new StatementApi()
      Given(s"get $caseName statement")

      val statement = statements.get(caseName)
      val json = JsonHelper.toJson(statement)

      When("the request is made")
      statementApi.post(json) match {
        case Success(code) =>
          Then("the request was successful")
          fail(s"Response code is ${code}")

        case Failure(code) =>
          Then("the LRS responds with HTTP 400")
      }
    }

    statements foreach { x => scenarioTemplate(x._1) }
  }
}
