package features.activityProfile

import com.arcusys.valamis.lrs.test.tincan.Activities
import features.BaseFeatureTests
import org.apache.commons.io.IOUtils
import org.apache.http.HttpStatus
import org.apache.http.client.entity.EntityBuilder
import org.apache.http.client.methods.{HttpDelete, HttpPut}
import org.apache.http.entity.ContentType
import org.apache.http.impl.client.HttpClients
import org.scalatest._

/**
 * Created by Iliya Tryapitsin on 18/02/15.
 */
@Ignore
class DeleteActivityProfile extends FeatureSpec with GivenWhenThen with BaseFeatureTests {
  feature("Delete activity profile") {
    val uri = uriBuilder.setPath("/activities/profile")
      .setParameter("activityId", Activities.typical.get.id.get.toString)
      .setParameter("profileId", "Activity profileId")
      .build()

    scenario("Good delete activityProfile: typical request cluster") {
      {
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

        val response = httpClient.execute(httpPut)

        assert(response.getStatusLine.getStatusCode == HttpStatus.SC_NO_CONTENT)
      }

      Given("a typical retrieveActivityProfile request cluster")
      val httpClient = HttpClients.createDefault()

      val httpDelete = new HttpDelete(uri)
      httpDelete.addHeader("X-Experience-API-Version", apiVersion)
      httpDelete.addHeader("Authorization", authString)

      When("the request is made on the primed LRS")
      val response = httpClient.execute(httpDelete)

      Then("the retrieveActivityProfile response is verified")
      assert(response.getStatusLine.getStatusCode == HttpStatus.SC_NO_CONTENT)
    }

    scenario("Bad delete activityProfile: typical request missing authority header") {
      Given("a typical retrieveActivityProfile request")
      val httpClient = HttpClients.createDefault()

      Given("the authority header is removed")
      val httpDelete = new HttpDelete(uri)
      httpDelete.addHeader("X-Experience-API-Version", apiVersion)

      When("the request is made")
      val response = httpClient.execute(httpDelete)

      Then("the LRS responds with HTTP 401")
      assert(response.getStatusLine.getStatusCode == HttpStatus.SC_UNAUTHORIZED)
    }

    scenario("Bad delete activityProfile: typical request missing activityId parameter") {
      Given("a typical retrieveActivityProfile request")
      val httpClient = HttpClients.createDefault()

      Given("the activityId parameter is removed")
      val uri = uriBuilder.setPath("/activities/profile")
        .removeQuery()
        .setParameter("profileId", "Activity profileId")
        .build()

      val httpDelete = new HttpDelete(uri)
      httpDelete.addHeader("X-Experience-API-Version", apiVersion)
      httpDelete.addHeader("Authorization", authString)

      When("the request is made")
      val response = httpClient.execute(httpDelete)

      Then("the LRS responds with HTTP 400")
      assert(response.getStatusLine.getStatusCode == HttpStatus.SC_BAD_REQUEST)
    }

    scenario("Bad delete state: resource request with bad resource 'activity/state'") {
      Given("a typical retrieveActivityProfile request")
      val httpClient = HttpClients.createDefault()

      Given("the resource is set to 'activity/state'")
      val uri = uriBuilder.setPath("/activity/state")
        .removeQuery()
        .setParameter("activityId", Activities.typical.get.id.toString)
        .setParameter("profileId", "Activity profileId")
        .build()

      val httpDelete = new HttpDelete(uri)
      httpDelete.addHeader("X-Experience-API-Version", apiVersion)
      httpDelete.addHeader("Authorization", authString)

      When("the request is made")
      val response = httpClient.execute(httpDelete)

      Then("the LRS responds with HTTP 400")
      assert(response.getStatusLine.getStatusCode == HttpStatus.SC_BAD_REQUEST)
    }

    scenario("Bad delete state: resource request with bad resource 'activities/states'") {
      Given("a typical retrieveActivityProfile request")
      val httpClient = HttpClients.createDefault()

      Given("the resource is set to 'activities/states'")
      val uri = uriBuilder.setPath("/activities/states")
        .removeQuery()
        .setParameter("activityId", Activities.typical.get.id.toString)
        .setParameter("profileId", "Activity profileId")
        .build()

      val httpDelete = new HttpDelete(uri)
      httpDelete.addHeader("X-Experience-API-Version", apiVersion)
      httpDelete.addHeader("Authorization", authString)

      When("the request is made")
      val response = httpClient.execute(httpDelete)

      Then("the LRS responds with HTTP 400")
      assert(response.getStatusLine.getStatusCode == HttpStatus.SC_BAD_REQUEST)

    }

    scenario("Bad delete state: version header request with bad resource 'bad version'") {
      Given("a typical retrieveActivityProfile request")
      val httpClient = HttpClients.createDefault()

      Given("the version header is set to 'bad version'")
      val httpDelete = new HttpDelete(uri)
      httpDelete.addHeader("X-Experience-API-Version", "bad version")
      httpDelete.addHeader("Authorization", authString)

      When("the request is made")
      val response = httpClient.execute(httpDelete)

      Then("the LRS responds with HTTP 400")
      assert(response.getStatusLine.getStatusCode == HttpStatus.SC_BAD_REQUEST)
    }

    scenario("Bad delete state: version header request with bad resource '3.8.0'") {
      Given("a typical retrieveActivityProfile request")
      val httpClient = HttpClients.createDefault()

      Given("the version header is set to '3.8.0'")
      val httpDelete = new HttpDelete(uri)
      httpDelete.addHeader("X-Experience-API-Version", "3.8.0")
      httpDelete.addHeader("Authorization", authString)

      When("the request is made")
      val response = httpClient.execute(httpDelete)

      Then("the LRS responds with HTTP 400")
      assert(response.getStatusLine.getStatusCode == HttpStatus.SC_BAD_REQUEST)

    }

    scenario("Bad delete activity profile: typical request with bad authority header 'Basic badAuth'") {
      Given("a typical saveActivityProfile request")
      val httpClient = HttpClients.createDefault()

      Given("the authority header parameter is set to 'Basic badAuth'")
      val httpDelete = new HttpDelete(uri)
      httpDelete.addHeader("X-Experience-API-Version", apiVersion)
      httpDelete.addHeader("Authorization", "Basic badAuth")

      When("the request is made")
      val response = httpClient.execute(httpDelete)

      Then("the LRS responds with HTTP 400")
      assert(response.getStatusLine.getStatusCode == HttpStatus.SC_BAD_REQUEST)
    }

    scenario("Bad delete activity profile: typical request with bad authority header 'Basic TnsHNWplME1YZnc0VzdLTHRIWTo0aDdBb253Ml85WU53vSZLNlVZ'") {
      Given("a typical saveActivityProfile request")
      val httpClient = HttpClients.createDefault()

      Given("the authority header parameter is set to 'Basic TnsHNWplME1YZnc0VzdLTHRIWTo0aDdBb253Ml85WU53vSZLNlVZ'")
      val httpDelete = new HttpDelete(uri)
      httpDelete.addHeader("X-Experience-API-Version", apiVersion)
      httpDelete.addHeader("Authorization", "Basic TnsHNWplME1YZnc0VzdLTHRIWTo0aDdBb253Ml85WU53vSZLNlVZ")

      When("the request is made")
      val response = httpClient.execute(httpDelete)

      Then("the LRS responds with HTTP 401")
      assert(response.getStatusLine.getStatusCode == HttpStatus.SC_UNAUTHORIZED)
    }

    scenario("Bad delete state: activityId parameter request with bad resource 'bad URI'") {
      Given("a typical retrieveActivityProfile request")
      val httpClient = HttpClients.createDefault()

      Given("the activityId parameter is set to 'bad URI'")
      val uri = uriBuilder.setPath("/activities/profile")
        .setParameter("activityId", "bad URI")
        .setParameter("profileId", "Activity profileId")
        .build()

      val httpDelete = new HttpDelete(uri)
      httpDelete.addHeader("X-Experience-API-Version", apiVersion)
      httpDelete.addHeader("Authorization", authString)

      When("the request is made")
      val response = httpClient.execute(httpDelete)

      Then("the LRS responds with HTTP 400")
      assert(response.getStatusLine.getStatusCode == HttpStatus.SC_BAD_REQUEST)

    }
  }
}
