package com.arcusys.valamis.lrs.api

import java.io.InputStream
import java.util.UUID

import com.arcusys.valamis.lrs.tincan.Constants
import org.apache.http.client.entity.EntityBuilder
import org.apache.http.client.methods._
import org.apache.http.entity.ContentType
import org.apache.http.{HttpHeaders, HttpStatus}
import org.joda.time.DateTime

import scala.io.Source
import scala.util.{Failure, Success, Try}

/**
 * Created by Iliya Tryapitsin on 15/02/15.
 */
class ActivityProfileApi(val oauthInvoker: Option[OAuthInvoker] = None)(implicit lrs: LrsSettings) extends BaseApi() {


  def put(inputStream: InputStream,
          activityId: UUID,
          profileId: String): Try[Int] = {
    val uri = uriBuilder
      .clearParameters()
      .setPath(s"/$path/$addressPathSuffix")
      .setParameter("activityId", activityId.toString)
      .setParameter("profileId", profileId)
      .build()

    val entity = EntityBuilder.create()
      .setStream(inputStream)
      .build()

    val httpPut = new HttpPut(uri)
    httpPut.addHeader(Constants.Headers.Version, lrs.version)
    httpPut.addHeader(HttpHeaders.AUTHORIZATION, lrs.auth.getAuthString)
    httpPut.addHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_OCTET_STREAM.toString)
    httpPut.setEntity(entity)

    getResponseCode(httpPut)
  }

  def put(data: String,
          activityId: UUID,
          profileId: String): Try[Int] = {
    val uri = uriBuilder
      .clearParameters()
      .setPath(s"/$path/$addressPathSuffix")
      .setParameter("activityId", activityId.toString)
      .setParameter("profileId", profileId)
      .build()

    val entity = EntityBuilder.create()
      .setText(data)
      .build()

    val httpPut = new HttpPut(uri)
    httpPut.addHeader(Constants.Headers.Version, lrs.version)
    httpPut.addHeader(HttpHeaders.AUTHORIZATION, lrs.auth.getAuthString)
    httpPut.addHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.toString)
    httpPut.setEntity(entity)

    getResponseCode(httpPut)
  }

  def get(activityId: UUID,
          since: Option[DateTime] = None): Try[Seq[String]] = {
    uriBuilder
      .clearParameters()
      .setPath(s"/$path/$addressPathSuffix")
      .setParameter("activityId", activityId.toString)

    val uri = since match {
      case Some(value) => uriBuilder.addParameter("since", value.toString).build()
      case None => uriBuilder.build()
    }

    val httpGet = new HttpGet(uri)
    httpGet.addHeader(Constants.Headers.Version, lrs.version)
    httpGet.addHeader(HttpHeaders.AUTHORIZATION, lrs.auth.getAuthString)

    val respContent = invokeHttpRequest(oauthInvoker, httpGet)
    respContent.map(fromJson[Seq[String]](_))
  }

  def get(activityId: UUID,
          profileId: String): Try[String] = {
    val uri = uriBuilder
      .clearParameters()
      .setPath(s"/$path/$addressPathSuffix")
      .setParameter("activityId", activityId.toString)
      .setParameter("profileId", profileId)
      .build()

    val httpGet = new HttpGet(uri)
    httpGet.addHeader(Constants.Headers.Version, lrs.version)
    httpGet.addHeader(HttpHeaders.AUTHORIZATION, lrs.auth.getAuthString)

    invokeHttpRequest(oauthInvoker, httpGet)
  }


  def delete(activityId: UUID,
             profileId: String): Try[Int] = {
    val uri = uriBuilder
      .clearParameters()
      .setPath(s"/$path/$addressPathSuffix")
      .setParameter("activityId", activityId.toString)
      .setParameter("profileId", profileId)
      .build()

    val httpDelete = new HttpDelete(uri)
    httpDelete.addHeader(Constants.Headers.Version, lrs.version)
    httpDelete.addHeader(HttpHeaders.AUTHORIZATION, lrs.auth.getAuthString)
    httpDelete.addHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_OCTET_STREAM.toString)

    getResponseCode(httpDelete)
  }

  private def getResponseCode(request: HttpRequestBase): Try[Int] = {
    oauthInvoker match {
      case None =>
        val response = httpClient.execute(request)
        try {
          val respCode = response.getStatusLine.getStatusCode
          if (respCode == HttpStatus.SC_NO_CONTENT)
            Success(HttpStatus.SC_NO_CONTENT)
          else
            buildFailure(response)
        } finally {
          response.close()
        }
      case Some(invoker) => invoker(request) map (_ => HttpStatus.SC_NO_CONTENT)
    }
  }

  val addressPathSuffix: String = "activities/profile"
}