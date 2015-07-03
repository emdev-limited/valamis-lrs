package com.arcusys.valamis.lrs.api.test

import javax.xml.bind.DatatypeConverter

import com.arcusys.valamis.lrs.api.{LrsAuthBasicSettings, LrsSettings}
import com.arcusys.valamis.lrs.tincan.TincanVersion
import org.apache.http.client.utils.URIBuilder

/**
 * Created by Iliya Tryapitsin on 13/02/15.
 */
trait BaseFeatureTests {
  val apiVersion = TincanVersion.ver101
  val login      = "e4f66d45-0ae9-4ca2-b85c-977836ae7c6a"
  val pass       = "97c2cf20-57a5-4b94-b0d4-9d2511a57ec5"
  val authString = s"Basic ${DatatypeConverter.printBase64Binary(s"$login:$pass".getBytes)}"

  val uriBuilder = new URIBuilder()
    .setScheme("http")
    .setHost("localhost")
    .setPort(8080)

  implicit val lrs = new LrsSettings(
    address = "http://localhost:8080/valamis-lrs-portlet/xapi",
    version = apiVersion.toString,
    auth    = new LrsAuthBasicSettings(
      login    = login,
      password = pass
    )
  )
}
