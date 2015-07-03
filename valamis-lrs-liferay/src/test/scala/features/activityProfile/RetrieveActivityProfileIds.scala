package features.activityProfile

import com.arcusys.valamis.lrs.test.tincan.Activities
import features.BaseFeatureTests
import org.apache.http.HttpStatus
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.HttpClients
import org.scalatest._

/**
 * Created by Iliya Tryapitsin on 14/02/15.
 */
@Ignore
class RetrieveActivityProfileIds extends FeatureSpec with GivenWhenThen with BaseFeatureTests with ActivityProfileTestUtils {

  feature("Retrieve activity profile id's") {
    val uri = uriBuilder.setPath("/activities/profile")
      .setParameter("activityId", Activities.typical.get.id.get.toString)
      .build()

    scenario("Good retrieve activityProfile ids: typical request cluster") {
      addTypicalActivityProfile("one")
      addTypicalActivityProfile("two")
      addTypicalActivityProfile("three")

      Given("a typical retrieveActivityProfile request cluster")
      val httpClient = HttpClients.createDefault()

      val httpGet = new HttpGet(uri)
      httpGet.addHeader("X-Experience-API-Version", apiVersion)
      httpGet.addHeader("Authorization", authString)

      When("the request is made on the primed LRS")
      val response = httpClient.execute(httpGet)

      Then("the retrieveActivityProfile response is verified")
      //TODO Add response content verification
      assert(response.getStatusLine.getStatusCode == HttpStatus.SC_OK)
    }

    scenario("Bad retrieve activityProfile ids: typical request missing authority header") {
      Given("a typical retrieveActivityProfile request")
      val httpClient = HttpClients.createDefault()

      Given("the authority header is removed")
      val httpGet = new HttpGet(uri)
      httpGet.addHeader("X-Experience-API-Version", apiVersion)

      When("the request is made")
      val response = httpClient.execute(httpGet)

      Then("the LRS responds with HTTP 401")
      assert(response.getStatusLine.getStatusCode == HttpStatus.SC_UNAUTHORIZED)
    }

    scenario("Bad retrieve activityProfile ids: typical request missing activityId parameter") {
      Given("a typical retrieveActivityProfile request")
      val httpClient = HttpClients.createDefault()

      Given("the activityId parameter is removed")
      val uri = uriBuilder.setPath("/activities/profile")
        .removeQuery()
        .setParameter("profileId", "Activity profileId")
        .build()

      val httpGet = new HttpGet(uri)
      httpGet.addHeader("X-Experience-API-Version", apiVersion)
      httpGet.addHeader("Authorization", authString)

      When("the request is made")
      val response = httpClient.execute(httpGet)

      Then("the LRS responds with HTTP 400")
      assert(response.getStatusLine.getStatusCode == HttpStatus.SC_BAD_REQUEST)
    }

    scenario("Bad retrieve activity profile ids: resource request with bad resource 'activity/state'") {
      Given("a typical retrieveActivityProfile request")
      val httpClient = HttpClients.createDefault()

      Given("the resource is set to 'activity/state'")
      val uri = uriBuilder.setPath("/activity/state")
        .removeQuery()
        .setParameter("activityId", Activities.typical.get.id.toString)
        .setParameter("profileId", "Activity profileId")
        .build()

      val httpGet = new HttpGet(uri)
      httpGet.addHeader("X-Experience-API-Version", apiVersion)
      httpGet.addHeader("Authorization", authString)

      When("the request is made")
      val response = httpClient.execute(httpGet)

      Then("the LRS responds with HTTP 400")
      assert(response.getStatusLine.getStatusCode == HttpStatus.SC_BAD_REQUEST)
    }

    scenario("Bad retrieve activity profile ids: resource request with bad resource 'activities/states'") {
      Given("a typical retrieveActivityProfile request")
      val httpClient = HttpClients.createDefault()

      Given("the resource is set to 'activities/states'")
      val uri = uriBuilder.setPath("/activities/states")
        .removeQuery()
        .setParameter("activityId", Activities.typical.get.id.toString)
        .setParameter("profileId", "Activity profileId")
        .build()

      val httpGet = new HttpGet(uri)
      httpGet.addHeader("X-Experience-API-Version", apiVersion)
      httpGet.addHeader("Authorization", authString)

      When("the request is made")
      val response = httpClient.execute(httpGet)

      Then("the LRS responds with HTTP 400")
      assert(response.getStatusLine.getStatusCode == HttpStatus.SC_BAD_REQUEST)

    }

    scenario("Bad retrieve activity profile ids: version header request with bad resource 'bad version'") {
      Given("a typical retrieveActivityProfile request")
      val httpClient = HttpClients.createDefault()

      Given("the version header is set to 'bad version'")
      val httpGet = new HttpGet(uri)
      httpGet.addHeader("X-Experience-API-Version", "bad version")
      httpGet.addHeader("Authorization", authString)

      When("the request is made")
      val response = httpClient.execute(httpGet)

      Then("the LRS responds with HTTP 400")
      assert(response.getStatusLine.getStatusCode == HttpStatus.SC_BAD_REQUEST)
    }

    scenario("Bad retrieve activity profile ids: version header request with bad resource '3.8.0'") {
      Given("a typical retrieveActivityProfile request")
      val httpClient = HttpClients.createDefault()

      Given("the version header is set to '3.8.0'")
      val httpGet = new HttpGet(uri)
      httpGet.addHeader("X-Experience-API-Version", "3.8.0")
      httpGet.addHeader("Authorization", authString)

      When("the request is made")
      val response = httpClient.execute(httpGet)

      Then("the LRS responds with HTTP 400")
      assert(response.getStatusLine.getStatusCode == HttpStatus.SC_BAD_REQUEST)

    }

    scenario("Bad retrieve activity profile ids: typical request with bad authority header 'Basic badAuth'") {
      Given("a typical saveActivityProfile request")
      val httpClient = HttpClients.createDefault()

      Given("the authority header parameter is set to 'Basic badAuth'")
      val httpGet = new HttpGet(uri)
      httpGet.addHeader("X-Experience-API-Version", apiVersion)
      httpGet.addHeader("Authorization", "Basic badAuth")

      When("the request is made")
      val response = httpClient.execute(httpGet)

      Then("the LRS responds with HTTP 400")
      assert(response.getStatusLine.getStatusCode == HttpStatus.SC_BAD_REQUEST)
    }

    scenario("Bad retrieve activity profile ids: typical request with bad authority header 'Basic TnsHNWplME1YZnc0VzdLTHRIWTo0aDdBb253Ml85WU53vSZLNlVZ'") {
      Given("a typical saveActivityProfile request")
      val httpClient = HttpClients.createDefault()

      Given("the authority header parameter is set to 'Basic TnsHNWplME1YZnc0VzdLTHRIWTo0aDdBb253Ml85WU53vSZLNlVZ'")
      val httpGet = new HttpGet(uri)
      httpGet.addHeader("X-Experience-API-Version", apiVersion)
      httpGet.addHeader("Authorization", "Basic TnsHNWplME1YZnc0VzdLTHRIWTo0aDdBb253Ml85WU53vSZLNlVZ")

      When("the request is made")
      val response = httpClient.execute(httpGet)

      Then("the LRS responds with HTTP 401")
      assert(response.getStatusLine.getStatusCode == HttpStatus.SC_UNAUTHORIZED)
    }

    scenario("Bad retrieve activity profile ids: activityId parameter request with bad resource 'bad URI'") {
      Given("a typical retrieveActivityProfile request")
      val httpClient = HttpClients.createDefault()

      Given("the activityId parameter is set to 'bad URI'")
      val uri = uriBuilder.setPath("/activities/profile")
        .setParameter("activityId", "bad URI")
        .setParameter("profileId", "Activity profileId")
        .build()

      val httpGet = new HttpGet(uri)
      httpGet.addHeader("X-Experience-API-Version", apiVersion)
      httpGet.addHeader("Authorization", authString)

      When("the request is made")
      val response = httpClient.execute(httpGet)

      Then("the LRS responds with HTTP 400")
      assert(response.getStatusLine.getStatusCode == HttpStatus.SC_BAD_REQUEST)

    }
  }
}
