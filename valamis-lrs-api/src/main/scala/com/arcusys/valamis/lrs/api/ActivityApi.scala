package com.arcusys.valamis.lrs.api

import com.arcusys.valamis.lrs.serializer.ActivitySerializer
import com.arcusys.valamis.lrs.tincan.{Activity, Constants}
import org.apache.http.util.EntityUtils
import org.apache.http.{HttpStatus, HttpHeaders}
import org.apache.http.client.methods.HttpGet

import scala.util._

/**
 * Created by Iliya Tryapitsin on 06.07.15.
 */
class ActivityApi(implicit lrs: LrsSettings) extends BaseApi() {
  def getActivities(activity: String): Try[String] = {
    val uri = uriBuilder
      .clearParameters()
      .setPath(s"/$path/$addressPathSuffix")
      .setParameter("activity", activity)
      .build()

    val httpGet = new HttpGet(uri)
    httpGet.addHeader(Constants.Headers.Version, lrs.version)
    httpGet.addHeader(HttpHeaders.AUTHORIZATION, lrs.auth.getAuthString)

    val response = httpClient.execute(httpGet)
    if (response.getStatusLine.getStatusCode == HttpStatus.SC_OK) {
      val content = EntityUtils.toString(response.getEntity)
      Success(content)
    } else Failure(new FailureRequestException(response.getStatusLine.getStatusCode))
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

    val response = httpClient.execute(httpGet)

    if (response.getStatusLine.getStatusCode == HttpStatus.SC_OK) {
      val content = EntityUtils.toString(response.getEntity)
      Success(fromJson[Activity](content, new ActivitySerializer))
    } else Failure(new FailureRequestException(response.getStatusLine.getStatusCode))
  }

  val addressPathSuffix: String = "activities"
}
