package com.arcusys.valamis.lrs

import org.apache.http.client.methods.HttpRequestBase

import scala.util.Try

/**
  * Created by pkornilov on 09.03.16.
  */
package object api {
  type OAuthInvoker = HttpRequestBase => Try[String]
}
