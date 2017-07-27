package com.arcusys.valamis.lrs.api

import com.arcusys.valamis.lrs.serializer.ActivitySerializer
import com.arcusys.valamis.lrs.tincan.{Activity, Constants}
import org.apache.http.HttpHeaders
import org.apache.http.client.methods.HttpGet

import scala.util._

/**
 * Created by Iliya Tryapitsin on 06.07.15.
 */
class ActivityApi(val oauthInvoker: Option[OAuthInvoker] = None)(implicit lrs: LrsSettings) extends BaseApi() {
  def getActivities(activity: String): Try[String] = {
    val uri = uriBuilder
      .clearParameters()
      .setPath(s"/$path/$addressPathSuffix")
      .setParameter("activity", activity)
      .build()

    val httpGet = new HttpGet(uri)
    httpGet.addHeader(Constants.Headers.Version, lrs.version)
    httpGet.addHeader(HttpHeaders.AUTHORIZATION, lrs.auth.getAuthString)

    invokeHttpRequest(oauthInvoker, httpGet)
  }

  def getActivity(activityId: String): Try[Activity] = {
    val uri = uriBuilder
      .clearParameters()
      .setPath(s"/$path/$addressPathSuffix")
      .setParameter("activityId", activityId)
      .build()

    val httpGet = new HttpGet(uri)
    httpGet.addHeader(Constants.Headers.Version, lrs.version)
    httpGet.addHeader(HttpHeaders.AUTHORIZATION, lrs.auth.getAuthString)

    val respContent = invokeHttpRequest(oauthInvoker, httpGet)
    respContent.map(fromJson[Activity](_, new ActivitySerializer))
  }

  val addressPathSuffix: String = "activities"
}
