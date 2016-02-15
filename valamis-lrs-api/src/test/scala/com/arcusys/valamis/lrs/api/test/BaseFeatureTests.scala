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
  val login      = "d77a64cc-ff5e-44d8-81f6-bfe289111ca1"
  val pass       = "0fcd2b57-264f-41ff-9c78-d0ca8f2ae459"
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
