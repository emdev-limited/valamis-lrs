package com.arcusys.valamis.lrs.liferay.filter

import java.io.StringBufferInputStream
import java.net.URLDecoder
import javax.servlet._
import javax.servlet.http.{HttpServletRequest, HttpServletRequestWrapper, HttpServletResponse}

import com.google.inject.Singleton

import scala.collection.JavaConverters._


@Singleton
class MethodOverrideFilter() extends Filter {
  private val Method = "method"

  override def doFilter(request: ServletRequest,
                        response: ServletResponse,
                        filterChain: FilterChain) = {

    val req = request.asInstanceOf[HttpServletRequest]
    val res = response.asInstanceOf[HttpServletResponse]

    val req2 = req.getMethod match {
      case "POST" =>
        req.getParameter(Method) match {
          case null => req
          case method => new HttpServletRequestWrapper(req) {
            private val encoding = req.getCharacterEncoding
            private val enc = if (encoding == null || encoding.trim.length == 0) {
              "ISO-8859-1"
            } else encoding
            private final val bodyContent = URLDecoder.decode(scala.io.Source.fromInputStream(req.getInputStream).mkString, enc)

            override def getMethod = method.toUpperCase

            override def getParameterMap: java.util.Map[String, Array[String]] = {
              bodyContent.split("&").map(param => {
                val paramSplit = param.split("=").toSeq
                (paramSplit.head, if (paramSplit.length == 2) Array[String](paramSplit.last) else Array[String]())
              }).filter(_._2 != null).toMap.asJava
            }

            override def getContentType = {
              bodyContent.split("&").find(param => {
                val paramSplit = param.split("=").toSeq
                paramSplit.head.equals("Content-Type")
              }).getOrElse("").split("=").last
            }

            override def getInputStream = {
              val content = bodyContent.split("&").find(param => {
                val paramSplit = param.split("=").toSeq
                paramSplit.head.equals("content")
              }).getOrElse("").split("=").last

              val byteArrayInputStream = new StringBufferInputStream(content)
              new ServletInputStream() {
                def read() = {
                  byteArrayInputStream.read
                }
              }
            }
          }
        }
      case _ =>
        req
    }
    filterChain.doFilter(req2, res)
  }

  override def init(filterConfig: FilterConfig) = Unit

  override def destroy() = Unit
}
