package com.arcusys.valamis.lrs.liferay.servlet.request

import javax.servlet.http.HttpServletRequest

import com.arcusys.valamis.lrs.liferay.exception.InvalidOrMissingArgumentException
import com.arcusys.json.JsonHelper
import com.arcusys.learn.liferay.lrs.util.PortalUtilHelper
import org.apache.commons.lang.CharEncoding
import org.json4s.DefaultFormats

import scala.collection.JavaConverters._
import scala.io.Source

class BaseLrsRequest(var request: HttpServletRequest) {

  implicit val formats = DefaultFormats


  /// Require
  def require(name: String): String = {
    val value = request.getParameter(name)
    if (value == null || value.isEmpty)
      throw new InvalidOrMissingArgumentException(name)
    value
  }

  def requireInt(name: String):     Int     = require(name) toInt

  def requireDouble(name: String):  Double  = require(name) toDouble

  def requireFloat(name: String):   Float   = require(name) toFloat

  def requireLong(name: String):    Long    = require(name) toLong

  def requireBoolean(name: String): Boolean = require(name) toBoolean


  /// Options
  def optional(name: String): Option[String] = {
    val value = request.getParameter(name)
    if (value == null || value.isEmpty)
      None
    else
      Some(value)
  }

  def optionalInt(name: String):     Option[Int]     = optional(name) map { v => v.toInt }

  def optionalDouble(name: String):  Option[Double]  = optional(name) map { v => v.toDouble }

  def optionalFloat(name: String):   Option[Float]   = optional(name) map { v => v.toFloat }

  def optionalLong(name: String):    Option[Long]    = optional(name) map { v => v.toLong }

  def optionalBoolean(name: String): Option[Boolean] = optional(name) map { v => v.toBoolean }


  /// Arrays
  def array(name: String):        Seq[String]  = request.getParameterValues(name)

  def arrayInt(name: String):     Seq[Int]     = request.getParameterValues(name) map { item => item.toInt }

  def arrayDouble(name: String):  Seq[Double]  = request.getParameterValues(name) map { item => item.toDouble }

  def arrayFloat(name: String):   Seq[Long]    = request.getParameterValues(name) map { item => item.toLong }

  def arrayBoolean(name: String): Seq[Boolean] = request.getParameterValues(name) map { item => item.toBoolean }


  def has(name: String): Boolean = request.getParameterNames.asScala.contains(name)

  def isMultipart = request.getContentType != null && request.getContentType.contains("multipart/")

  def getRequestModel[T](request: HttpServletRequest)(implicit manifest: Manifest[T]) = JsonHelper.fromJson[T](body)

  def contentType: Option[String] = Option(request.getContentType)

  def body: String = {
    val uploadRequest = PortalUtilHelper.getUploadServletRequest(request)
    val encoding = uploadRequest.getCharacterEncoding
    val enc = if (encoding == null || encoding.trim.length == 0) CharEncoding.UTF_8
    else encoding

    val stream = uploadRequest.getInputStream
    val body = Source.fromInputStream(stream, enc).mkString
    body

  }
}