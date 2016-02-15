package com.arcusys.valamis.lrs.api.test

import java.net.URI

import com.arcusys.json.JsonHelper
import com.arcusys.valamis.lrs._
import com.arcusys.valamis.lrs.api.valamis.ScaleApi
import com.arcusys.valamis.lrs.serializer.AgentSerializer
import com.arcusys.valamis.lrs.tincan.Agent
import org.scalatest.{Ignore, FeatureSpec}

import scala.util._

/**
 * Created by Iliya Tryapitsin on 16.06.15.
 */
class ScaleApiTest extends FeatureSpec with BaseFeatureTests {
  ignore("Get max activity scaled score") {
    val verbApi = new ScaleApi
    val agent = JsonHelper.toJson(
      Agent (mBox = "mailto:tincanpython@tincanapi.com" ?), new AgentSerializer
    )

    val verb  = new URI ("http://adlnet.gov/expapi/verbs/experienced")

    val scale = verbApi.getMaxActivityScale(agent, verb) match {
      case Success(v) => v
      case Failure(e) => fail(e)
    }
  }
}
