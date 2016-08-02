package com.arcusys.valamis.lrs.api.valamis

import java.net.URI

import com.arcusys.valamis.lrs.api._
import com.arcusys.valamis.lrs.tincan.Activity
import org.apache.http.client.methods.HttpGet

import scala.util.Try

/**
 * Created by Iliya Tryapitsin on 21.07.15.
 */
class ScaleApi(val oauthInvoker: Option[OAuthInvoker] = None)(implicit lrs: LrsSettings) extends BaseApi() {

  val addressPathSuffix = "valamis/scale"

  private val Verb  = "verb"
  private val Agent = "agent"
  private val ActivityScale = "activity-scale"
  private val Action  = "action"

  def getMaxActivityScale(agent: String, verb: URI): Try[Seq[(Activity#Id, Option[Float])]] = {
    val uri = uriBuilder
      .clearParameters()
      .setPath(s"/$path/$addressPathSuffix")
      .addParameter(Agent, agent)
      .addParameter(Verb, verb toString)
      .addParameter(Action, ActivityScale)
      .build()

    val httpGet = new HttpGet(uri)
    initRequestAsJson(httpGet)

    val respContent = invokeHttpRequest(oauthInvoker, httpGet)
    respContent map { json =>
      fromJson[Seq[(Activity#Id, Option[Float])]](json)
    }
  }
}
