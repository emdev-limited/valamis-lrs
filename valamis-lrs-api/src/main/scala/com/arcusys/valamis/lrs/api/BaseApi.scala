package com.arcusys.valamis.lrs.api

import java.io.InputStream

import com.arcusys.valamis.lrs.tincan.Constants
import org.apache.http.{HttpStatus, HttpHeaders}
import org.apache.http.client.methods.{CloseableHttpResponse, HttpRequestBase}
import org.apache.http.client.utils.URIBuilder
import org.apache.http.entity.ContentType
import org.apache.http.impl.client.HttpClients

import scala.io.Source
import scala.util.{Try, Failure, Success}

/**
 * Created by Iliya Tryapitsin on 19/02/15.
 */
abstract class BaseApi(implicit lrs: LrsSettings) {

  def addressPathSuffix: String

  protected val httpClient = HttpClients.createDefault()
  protected val uriBuilder = new URIBuilder(lrs.address)
  protected val path = uriBuilder.getPath

  protected def initRequestAsJson(request: HttpRequestBase) = {
    request.addHeader(Constants.Headers.Version, lrs.version)
    request.addHeader(HttpHeaders.AUTHORIZATION, lrs.auth.getAuthString)
    request.addHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.toString)
  }

  protected def getContent(response: CloseableHttpResponse) : Try[String] = {

    var stream : InputStream = null
    try {
      if (response.getStatusLine.getStatusCode == HttpStatus.SC_OK) {
        stream = response.getEntity.getContent
        Success(Source.fromInputStream(stream).mkString)
      } else {
        Failure(new FailureRequestException(response.getStatusLine.getStatusCode))
      }
    } finally {
      response.close()
      if (stream != null) stream.close()
    }
  }

  implicit class BuilderExtension(builder: URIBuilder) {
    def addOptionParameter[T](name: String, v: Option[T]) = v match {
      case Some(value) => builder.addParameter(name, value toString)
      case None        => builder
    }
  }

  def close(): Unit = {
    httpClient.close()
  }
}
