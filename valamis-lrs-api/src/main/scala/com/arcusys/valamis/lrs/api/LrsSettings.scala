package com.arcusys.valamis.lrs.api

import javax.xml.bind.DatatypeConverter

case class LrsSettings(address:String, version:String, auth: LrsAuthSettings)

trait LrsAuthSettings {
  def getAuthString : String
}

class LrsAuthDefaultSettings(authLine: String) extends LrsAuthSettings{
  override def getAuthString = authLine
}

class LrsAuthBasicSettings(login:String, password:String) extends LrsAuthSettings{
  override def getAuthString = "Basic " + DatatypeConverter.printBase64Binary((login + ":" + password).getBytes)
}

class LrsAuthOAuthSettings(val authLine:String) extends LrsAuthSettings{
  override def getAuthString = "OAuth " + authLine
}

class LrsAuthOAuth2Settings(val token:String) extends LrsAuthSettings{
  override def getAuthString = "Bearer " + token
}