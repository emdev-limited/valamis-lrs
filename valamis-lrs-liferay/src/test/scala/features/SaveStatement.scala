package features

import java.util.UUID

import com.arcusys.valamis.lrs.test.tincan.Statements
import com.arcusys.json.JsonHelper
import com.arcusys.valamis.lrs.api._
import org.scalatest._

import scala.io.Source
import scala.util.{Failure, Success}

/**
 * Created by Iliya Tryapitsin on 13/02/15.
 */
@Ignore
class SaveStatement extends FeatureSpec with GivenWhenThen with BaseFeatureTests {

  def loadDataFromTxtResource(path: String) = Source.fromURL(getClass.getResource(path)).getLines().mkString

  feature("Save statement") {

    val uri = uriBuilder.setPath("/statements")
      .build()

    scenario("Good save statements: typical request") {

      Given("a typical statement")
      val json = JsonHelper.toJson(Statements.Good.minimal)

      val statementApi = new StatementApi()

      When("the request is made")
      statementApi.post(json) match {
        case Success(code) => {
          Then("the LRS responds with HTTP 200")
          println(s"Response code is $code")
        }
        case Failure(code) => {
          Then(s"the LRS responds with HTTP $code")
          fail(s"Response code is $code")
        }
      }
    }

    scenario("Good save statements: minimal request") {

      Given("a minimal statement")
      val json = JsonHelper.toJson(Statements.Good.minimal)

      val statementApi = new StatementApi()

      When("the request is made")
      statementApi.post(json) match {
        case Success(code) => {
          Then("the LRS responds with HTTP 200")
          println(s"Response code is $code")
        }
        case Failure(code) => {
          Then("the LRS responds with HTTP 200")
          fail(s"Response code is $code")
        }
      }
    }

    ignore("Good save statements: case #1 request") {

      Given("a minimal statement")
      val json = loadDataFromTxtResource("/statements/statement-1.json")

      val statementApi = new StatementApi()

      When("the request is made")
      statementApi.put(UUID.fromString("8d98bb1c-1301-4436-af23-cae7a1d89c42"), json) match {
        case Success(code) => {
          Then("the LRS responds with HTTP 200")
          println(s"Response code is $code")
        }
        case Failure(ex: FailureRequestException) => {
          Then("the LRS responds with HTTP 400")
          fail(s"Response code is ${ex.responseCode}")
        }
      }
    }
  }

  //  TODO: Fix multipart implementation
  //  ignore should "Given a attachment saveStatement request When the request is made Then the LRS responds with HTTP 204" in {
  //    val statement = Statements.attachment
  //    val attachment = Attachments.text
  //    val json = JsonHelper.toJson(statement)
  //
  //    val httpClient = HttpClients.createDefault()
  //    val httpPut = new HttpPost(s"$targetHostAddress/statements")
  //
  //    httpPut.addHeader("X-Experience-API-Version", apiVersion)
  //    httpPut.addHeader("Authorization", authString)
  //    httpPut.addHeader("Content-Type", "multipart/mixed")
  //
  //    val attachmentPart = new FormBodyPart("attachments", new StringBody(attachment.content.get, ContentType.TEXT_PLAIN))
  //    attachmentPart.addField("X-Experience-API-Hash", attachment.statementMetadata.sha2)
  //
  //    val entity = MultipartEntityBuilder.create().setBoundary("abcABC0123'()+_,-./:=?")
  //      .addTextBody("statement", json, ContentType.APPLICATION_JSON)
  //      .addPart(attachmentPart)
  //      .build()
  //
  //    httpPut.setEntity(entity)
  //
  //    val response = httpClient.execute(httpPut)
  //
  //    assert(response.getStatusLine.getStatusCode == HttpStatus.SC_NO_CONTENT)
  //  }
}
