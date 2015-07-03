package com.arcusys.valamis.lrs.api.test

import com.arcusys.valamis.lrs.api.valamis.VerbApi
import org.scalatest.{Ignore, FeatureSpec}

import scala.util._

/**
 * Created by Iliya Tryapitsin on 16.06.15.
 */
class VerbApiTest extends FeatureSpec with BaseFeatureTests {
  ignore("get verb's statistics") {
    val verbApi = new VerbApi
    val verbs = verbApi.getStatistics() match {
      case Success(v) => v
      case Failure(e) => fail(e)
    }
  }

  ignore("get verbs with activities") {
    val verbApi = new VerbApi
    val result = verbApi.getWithActivities(Some("exp")) match {
      case Success(v) => v
      case Failure(e) => e
    }
  }
}
