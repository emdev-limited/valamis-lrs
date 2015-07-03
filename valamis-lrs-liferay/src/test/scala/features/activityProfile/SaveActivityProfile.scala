package features.activityProfile

import com.arcusys.valamis.lrs.test.tincan.{Activities, Contents}
import features.BaseFeatureTests
import org.apache.commons.io.IOUtils
import org.apache.http.HttpStatus
import org.apache.http.client.entity.EntityBuilder
import org.apache.http.client.methods.{HttpPost, HttpPut}
import org.apache.http.entity.ContentType
import org.apache.http.impl.client.HttpClients
import org.scalatest._

/**
 * Created by Iliya Tryapitsin on 13/02/15.
 */
@Ignore
class SaveActivityProfile extends FeatureSpec with GivenWhenThen with BaseFeatureTests {
  feature("Save activity profile") {
    val uri = uriBuilder.setPath("/activities/profile")
      .setParameter("activityId", Activities.typical.get.id.get.toString)
      .setParameter("profileId", "Activity profileId")
      .build()

    scenario("Good save activity profile: typical request") {
      Given("a typical saveActivityProfile request")
      val httpClient = HttpClients.createDefault()
      val inputStream = IOUtils.toInputStream("some content", "UTF-8")

      val entity = EntityBuilder.create()
        .setStream(inputStream)
        .build()

      val httpPut = new HttpPut(uri)
      httpPut.addHeader("X-Experience-API-Version", apiVersion)
      httpPut.addHeader("Authorization", authString)
      httpPut.addHeader("Content-Type", ContentType.APPLICATION_OCTET_STREAM.toString)
      httpPut.setEntity(entity)

      When("the request is made")
      val response = httpClient.execute(httpPut)

      Then("the LRS responds with HTTP 204")
      assert(response.getStatusLine.getStatusCode == HttpStatus.SC_NO_CONTENT)
    }

    scenario("Good save activity profile: JSON request") {
      Given("a JSON saveActivityProfile request")
      val httpClient = HttpClients.createDefault()
      val entity = EntityBuilder.create()
        .setText(Contents.json)
        .build()

      val httpPut = new HttpPut(uri)
      httpPut.addHeader("X-Experience-API-Version", apiVersion)
      httpPut.addHeader("Authorization", authString)
      httpPut.addHeader("Content-Type", ContentType.APPLICATION_JSON.toString)
      httpPut.setEntity(entity)

      When("the request is made")
      val response = httpClient.execute(httpPut)

      Then("the LRS responds with HTTP 204")
      assert(response.getStatusLine.getStatusCode == HttpStatus.SC_NO_CONTENT)
    }

    scenario("Good save activity profile: typical request missing Content-Type header") {

      Given("a typical saveActivityProfile request")
      val httpClient = HttpClients.createDefault()
      val entity = EntityBuilder.create()
        .setText("some content")
        .build()

      Given("the Content-Type header is removed")
      val httpPut = new HttpPut(uri)
      httpPut.addHeader("X-Experience-API-Version", apiVersion)
      httpPut.addHeader("Authorization", authString)
      httpPut.setEntity(entity)

      When("the request is made")
      val response = httpClient.execute(httpPut)

      Then("the LRS responds with HTTP 204")
      assert(response.getStatusLine.getStatusCode == HttpStatus.SC_NO_CONTENT)
    }

    scenario("Good save activity profile: typical request missing content") {

      Given("a typical saveActivityProfile request")
      val httpClient = HttpClients.createDefault()

      val httpPut = new HttpPut(uri)
      httpPut.addHeader("X-Experience-API-Version", apiVersion)
      httpPut.addHeader("Authorization", authString)
      httpPut.addHeader("Content-Type", ContentType.APPLICATION_OCTET_STREAM.toString)

      Given("the content is removed")

      When("the request is made")
      val response = httpClient.execute(httpPut)

      Then("the LRS responds with HTTP 204")
      assert(response.getStatusLine.getStatusCode == HttpStatus.SC_NO_CONTENT)
    }

    scenario("Good save activity profile: JSON request missing content") {

      Given("a typical saveActivityProfile request")
      val httpClient = HttpClients.createDefault()

      val httpPut = new HttpPut(uri)
      httpPut.addHeader("X-Experience-API-Version", apiVersion)
      httpPut.addHeader("Authorization", authString)
      httpPut.addHeader("Content-Type", ContentType.APPLICATION_JSON.toString)

      Given("the content is removed")

      When("the request is made")
      val response = httpClient.execute(httpPut)

      Then("the LRS responds with HTTP 204")
      assert(response.getStatusLine.getStatusCode == HttpStatus.SC_NO_CONTENT)
    }

    scenario("Good save activity profile: typical request with Content-Type header set to 'test content type'") {
      Given("a typical saveActivityProfile request")
      val httpClient = HttpClients.createDefault()

      val httpPut = new HttpPut(uri)
      httpPut.addHeader("X-Experience-API-Version", apiVersion)
      httpPut.addHeader("Authorization", authString)

      Given("the Content-Type header is set to 'test content type'")
      httpPut.addHeader("Content-Type", "test content type")

      When("the request is made")
      val response = httpClient.execute(httpPut)

      Then("the LRS responds with HTTP 204")
      assert(response.getStatusLine.getStatusCode == HttpStatus.SC_NO_CONTENT)
    }

    scenario("Good save activity profile: typical request with content set to 'test content'") {
      Given("a typical saveActivityProfile request")
      val httpClient = HttpClients.createDefault()

      val httpPut = new HttpPut(uri)
      httpPut.addHeader("X-Experience-API-Version", apiVersion)
      httpPut.addHeader("Authorization", authString)
      httpPut.addHeader("Content-Type", ContentType.APPLICATION_OCTET_STREAM.toString)

      Given("the content is set to 'test content'")
      val inputStream = IOUtils.toInputStream("test content", "UTF-8")

      val entity = EntityBuilder.create()
        .setStream(inputStream)
        .build()
      httpPut.setEntity(entity)

      When("the request is made")
      val response = httpClient.execute(httpPut)

      Then("the LRS responds with HTTP 204")
      assert(response.getStatusLine.getStatusCode == HttpStatus.SC_NO_CONTENT)
    }

    scenario("Good save activity profile: JSON request with method set to 'POST'") {
      Given("a JSON saveActivityProfile request")
      val httpClient = HttpClients.createDefault()
      val entity = EntityBuilder.create()
        .setText(Contents.json)
        .build()

      Given("the method is set to 'POST'")
      val httpPost = new HttpPost(uri)
      httpPost.addHeader("X-Experience-API-Version", apiVersion)
      httpPost.addHeader("Authorization", authString)
      httpPost.addHeader("Content-Type", ContentType.APPLICATION_JSON.toString)
      httpPost.setEntity(entity)

      When("the request is made")
      val response = httpClient.execute(httpPost)

      Then("the LRS responds with HTTP 204")
      assert(response.getStatusLine.getStatusCode == HttpStatus.SC_NO_CONTENT)
    }

    scenario("Bad save activity profile: typical request missing authority header") {
      Given("a typical saveActivityProfile request")
      val httpClient = HttpClients.createDefault()
      val inputStream = IOUtils.toInputStream("some content", "UTF-8")

      val entity = EntityBuilder.create()
        .setStream(inputStream)
        .build()

      val httpPut = new HttpPut(uri)
      httpPut.addHeader("X-Experience-API-Version", apiVersion)
      httpPut.addHeader("Authorization", authString)
      httpPut.addHeader("Content-Type", ContentType.APPLICATION_OCTET_STREAM.toString)
      httpPut.setEntity(entity)

      Given("the authority header is removed")
      httpPut.removeHeaders("Authorization")

      When("the request is made")
      val response = httpClient.execute(httpPut)

      Then("the LRS responds with HTTP 401")
      assert(response.getStatusLine.getStatusCode == HttpStatus.SC_UNAUTHORIZED)
    }

    scenario("Bad save activity profile: typical request missing activityId parameter") {
      Given("a typical saveActivityProfile request")
      val httpClient = HttpClients.createDefault()
      val inputStream = IOUtils.toInputStream("some content", "UTF-8")

      val entity = EntityBuilder.create()
        .setStream(inputStream)
        .build()

      Given("the activityId parameter is removed")
      val uri = uriBuilder.setPath("/activities/profile")
        .removeQuery()
        .setParameter("profileId", "Activity profileId")
        .build()

      val httpPut = new HttpPut(uri)
      httpPut.addHeader("X-Experience-API-Version", apiVersion)
      httpPut.addHeader("Authorization", authString)
      httpPut.addHeader("Content-Type", ContentType.APPLICATION_OCTET_STREAM.toString)
      httpPut.setEntity(entity)

      When("the request is made")
      val response = httpClient.execute(httpPut)

      Then("the LRS responds with HTTP 400")
      assert(response.getStatusLine.getStatusCode == HttpStatus.SC_BAD_REQUEST)

    }

    scenario("Bad save activity profile: typical request with bad resource 'activity/state'") {
      Given("a typical saveActivityProfile request")
      val httpClient = HttpClients.createDefault()
      val inputStream = IOUtils.toInputStream("some content", "UTF-8")

      val entity = EntityBuilder.create()
        .setStream(inputStream)
        .build()

      Given("the recource parameter is set to 'activity/state'")
      val uri = uriBuilder.setPath("/activity/state")
        .removeQuery()
        .setParameter("activityId", Activities.typical.get.id.toString)
        .setParameter("profileId", "Activity profileId")
        .build()

      val httpPut = new HttpPut(uri)
      httpPut.addHeader("X-Experience-API-Version", apiVersion)
      httpPut.addHeader("Authorization", authString)
      httpPut.addHeader("Content-Type", ContentType.APPLICATION_OCTET_STREAM.toString)
      httpPut.setEntity(entity)

      When("the request is made")
      val response = httpClient.execute(httpPut)

      Then("the LRS responds with HTTP 400")
      assert(response.getStatusLine.getStatusCode == HttpStatus.SC_BAD_REQUEST)
    }

    scenario("Bad save activity profile: typical request with bad resource 'activities/states'") {
      Given("a typical saveActivityProfile request")
      val httpClient = HttpClients.createDefault()
      val inputStream = IOUtils.toInputStream("some content", "UTF-8")

      val entity = EntityBuilder.create()
        .setStream(inputStream)
        .build()

      Given("the recource parameter is set to 'activities/states'")
      val uri = uriBuilder.setPath("/activities/states")
        .removeQuery()
        .setParameter("activityId", Activities.typical.get.id.toString)
        .setParameter("profileId", "Activity profileId")
        .build()

      val httpPut = new HttpPut(uri)
      httpPut.addHeader("X-Experience-API-Version", apiVersion)
      httpPut.addHeader("Authorization", authString)
      httpPut.addHeader("Content-Type", ContentType.APPLICATION_OCTET_STREAM.toString)
      httpPut.setEntity(entity)

      When("the request is made")
      val response = httpClient.execute(httpPut)

      Then("the LRS responds with HTTP 400")
      assert(response.getStatusLine.getStatusCode == HttpStatus.SC_BAD_REQUEST)
    }

    scenario("Bad save activity profile: typical request with bad resource 'activities'") {
      Given("a typical saveActivityProfile request")
      val httpClient = HttpClients.createDefault()
      val inputStream = IOUtils.toInputStream("some content", "UTF-8")

      val entity = EntityBuilder.create()
        .setStream(inputStream)
        .build()

      Given("the recource parameter is set to 'activityies'")
      val uri = uriBuilder.setPath("/activities")
        .removeQuery()
        .setParameter("activityId", Activities.typical.get.id.toString)
        .setParameter("profileId", "Activity profileId")
        .build()

      val httpPut = new HttpPut(uri)
      httpPut.addHeader("X-Experience-API-Version", apiVersion)
      httpPut.addHeader("Authorization", authString)
      httpPut.addHeader("Content-Type", ContentType.APPLICATION_OCTET_STREAM.toString)
      httpPut.setEntity(entity)

      When("the request is made")
      val response = httpClient.execute(httpPut)

      Then("the LRS responds with HTTP 405")
      assert(response.getStatusLine.getStatusCode == HttpStatus.SC_METHOD_NOT_ALLOWED)
    }

    scenario("Bad save activity profile: typical request with bad version header 'bad version'") {
      Given("a typical saveActivityProfile request")
      val httpClient = HttpClients.createDefault()
      val inputStream = IOUtils.toInputStream("some content", "UTF-8")

      val entity = EntityBuilder.create()
        .setStream(inputStream)
        .build()

      Given("the version header parameter is set to 'bad version'")
      val httpPut = new HttpPut(uri)
      httpPut.addHeader("X-Experience-API-Version", "bad version")
      httpPut.addHeader("Authorization", authString)
      httpPut.addHeader("Content-Type", ContentType.APPLICATION_OCTET_STREAM.toString)
      httpPut.setEntity(entity)

      When("the request is made")
      val response = httpClient.execute(httpPut)

      Then("the LRS responds with HTTP 400")
      assert(response.getStatusLine.getStatusCode == HttpStatus.SC_BAD_REQUEST)
    }

    scenario("Bad save activity profile: typical request with bad version header '3.8.0'") {
      Given("a typical saveActivityProfile request")
      val httpClient = HttpClients.createDefault()
      val inputStream = IOUtils.toInputStream("some content", "UTF-8")

      val entity = EntityBuilder.create()
        .setStream(inputStream)
        .build()

      Given("the version header parameter is set to '3.8.0'")
      val httpPut = new HttpPut(uri)
      httpPut.addHeader("X-Experience-API-Version", "3.8.0")
      httpPut.addHeader("Authorization", authString)
      httpPut.addHeader("Content-Type", ContentType.APPLICATION_OCTET_STREAM.toString)
      httpPut.setEntity(entity)

      When("the request is made")
      val response = httpClient.execute(httpPut)

      Then("the LRS responds with HTTP 400")
      assert(response.getStatusLine.getStatusCode == HttpStatus.SC_BAD_REQUEST)
    }

    scenario("Bad save activity profile: typical request with bad authority header 'Basic badAuth'") {
      Given("a typical saveActivityProfile request")
      val httpClient = HttpClients.createDefault()
      val inputStream = IOUtils.toInputStream("some content", "UTF-8")

      val entity = EntityBuilder.create()
        .setStream(inputStream)
        .build()

      Given("the authority header parameter is set to 'Basic badAuth'")
      val httpPut = new HttpPut(uri)
      httpPut.addHeader("X-Experience-API-Version", apiVersion)
      httpPut.addHeader("Authorization", "Basic badAuth")
      httpPut.addHeader("Content-Type", ContentType.APPLICATION_OCTET_STREAM.toString)
      httpPut.setEntity(entity)

      When("the request is made")
      val response = httpClient.execute(httpPut)

      Then("the LRS responds with HTTP 400")
      assert(response.getStatusLine.getStatusCode == HttpStatus.SC_BAD_REQUEST)
    }

    scenario("Bad save activity profile: typical request with bad authority header 'Basic TnsHNWplME1YZnc0VzdLTHRIWTo0aDdBb253Ml85WU53vSZLNlVZ'") {
      Given("a typical saveActivityProfile request")
      val httpClient = HttpClients.createDefault()
      val inputStream = IOUtils.toInputStream("some content", "UTF-8")

      val entity = EntityBuilder.create()
        .setStream(inputStream)
        .build()

      Given("the authority header parameter is set to 'Basic TnsHNWplME1YZnc0VzdLTHRIWTo0aDdBb253Ml85WU53vSZLNlVZ'")
      val httpPut = new HttpPut(uri)
      httpPut.addHeader("X-Experience-API-Version", apiVersion)
      httpPut.addHeader("Authorization", "Basic TnsHNWplME1YZnc0VzdLTHRIWTo0aDdBb253Ml85WU53vSZLNlVZ")
      httpPut.addHeader("Content-Type", ContentType.APPLICATION_OCTET_STREAM.toString)
      httpPut.setEntity(entity)

      When("the request is made")
      val response = httpClient.execute(httpPut)

      Then("the LRS responds with HTTP 401")
      assert(response.getStatusLine.getStatusCode == HttpStatus.SC_UNAUTHORIZED)
    }

    scenario("Bad save activity profile: typical request with bad method 'POST'") {
      Given("a typical saveActivityProfile request")
      val httpClient = HttpClients.createDefault()
      val inputStream = IOUtils.toInputStream("some content", "UTF-8")

      val entity = EntityBuilder.create()
        .setStream(inputStream)
        .build()

      Given("the method is set to 'POST'")
      val httpPost = new HttpPost(uri)
      httpPost.addHeader("X-Experience-API-Version", apiVersion)
      httpPost.addHeader("Authorization", authString)
      httpPost.addHeader("Content-Type", ContentType.APPLICATION_OCTET_STREAM.toString)
      httpPost.setEntity(entity)

      When("the request is made")
      val response = httpClient.execute(httpPost)

      Then("the LRS responds with HTTP 400")
      //assert(response.getStatusLine.getStatusCode == HttpStatus.SC_BAD_REQUEST)
    }

    scenario("Bad save activity profile: typical request with bad activityId parameter 'bad URI'") {
      Given("a typical saveActivityProfile request")
      val httpClient = HttpClients.createDefault()
      val inputStream = IOUtils.toInputStream("some content", "UTF-8")

      val entity = EntityBuilder.create()
        .setStream(inputStream)
        .build()

      Given("the activityId parameter is set to 'bad URI'")
      val uri = uriBuilder.setPath("/activities/profile")
        .setParameter("activityId", "bad URI")
        .setParameter("profileId", "Activity profileId")
        .build()

      val httpPut = new HttpPut(uri)
      httpPut.addHeader("X-Experience-API-Version", apiVersion)
      httpPut.addHeader("Authorization", authString)
      httpPut.addHeader("Content-Type", ContentType.APPLICATION_OCTET_STREAM.toString)
      httpPut.setEntity(entity)

      When("the request is made")
      val response = httpClient.execute(httpPut)

      Then("the LRS responds with HTTP 400")
      assert(response.getStatusLine.getStatusCode == HttpStatus.SC_BAD_REQUEST)
    }
  }
}

//feature("Save activity profile") {
//scenario("Good save activity profile: typical request") {
//Given("a typical saveActivityProfile request")
//
//val uri = uriBuilder.setPath("/activities/profile")
//.setParameter("activityId", Activities.typical.get.id.toString)
//.setParameter("profileId", "Activity profileId")
//.build()
//
//val httpGet = new HttpGet(uri)
//httpGet.addHeader("X-Experience-API-Version", apiVersion)
//httpGet.addHeader("Authorization", authString
//
//val httpClient = HttpClients.createDefault()
//
//When("the request is made")
//val response = httpClient.execute(httpGet)
//
//Then("the LRS responds with HTTP 204")
//assert(response.getStatusLine.getStatusCode == HttpStatus.SC_OK)
//}
//}