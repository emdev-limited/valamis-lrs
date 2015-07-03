package features.activityProfile

import com.arcusys.valamis.lrs.test.tincan.Activities
import features.BaseFeatureTests
import org.scalatest._

/**
 * Created by Iliya Tryapitsin on 18/02/15.
 */
@Ignore
class ActivityProfileMerging extends FeatureSpec with GivenWhenThen with BaseFeatureTests with ActivityProfileTestUtils {
  ignore("Activity profile merging") {
    val uri = uriBuilder.setPath("/activities/profile")
      .setParameter("activityId", Activities.typical.get.id.get.toString)
      .setParameter("profileId", "profile Id")
      .build()

    scenario("Good activity profile merging: shallow request cluster") {

      Given("a shallow activityProfileMerging request cluster")

      When("the request is made on the primed LRS")

      Then("the activityProfileMerging response is verified")
    }

    scenario("Good activity profile merging: deep request cluster") {

      Given("a deep activityProfileMerging request cluster")

      When("the request is made on the primed LRS")

      Then("the activityProfileMerging response is verified")
    }

    scenario("Bad activity profile merging: mergeJSONWithNotJSON request cluster") {

      Given("a mergeJSONWithNotJSON activityProfileMerging request cluster")

      When("the request is made on the primed LRS")

      Then("the LRS responds with HTTP 400")

      Then("the activityProfileMerging response is verified")
    }

    scenario("Bad activity profile merging: mergeNotJSONWithJSON request cluster") {

      Given("a mergeNotJSONWithJSON activityProfileMerging request cluster")

      When("the request is made on the primed LRS")

      Then("the LRS responds with HTTP 400")

      Then("the activityProfileMerging response is verified")
    }
  }
}
