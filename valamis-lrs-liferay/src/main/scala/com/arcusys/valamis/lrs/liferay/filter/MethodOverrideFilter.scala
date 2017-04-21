package com.arcusys.valamis.lrs.liferay.filter

import java.io.ByteArrayInputStream
import java.net.{URLDecoder, URLEncoder}
import javax.servlet._
import javax.servlet.http.{HttpServletRequest, HttpServletRequestWrapper, HttpServletResponse}

import com.arcusys.valamis.lrs.liferay.util.DelegatingServletInputStream
import com.google.inject.Singleton
import org.apache.commons.lang.CharEncoding

import scala.collection.JavaConverters._
import scala.io.Source
import com.arcusys.valamis.lrs.utils._


@Singleton
class MethodOverrideFilter() extends Filter {
  private val Method = "method"
  private val Content = "content"
  private val ContentType = "Content-Type"
  private val TincanHeaders = Seq("authorization", "content-type", "x-experience-api-version", Content)


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
              "UTF-8"
            } else encoding

            private val ParamCount = 2
            private final val bodyContent = Source.fromInputStream(req.getInputStream).mkString
            private val newParameters = bodyContent.split("&").filterNot(_.isEmpty)
              .map(_.split("=", ParamCount).toSeq)
              .map(p => if(p.size == ParamCount) (p.head, p.last) else (p.head, EmptyString))
              .map(p => (URLDecoder.decode(p._1, enc), URLDecoder.decode(p._2, enc))).toMap


            private def getNewParameter(name: String): Option[String] = newParameters.find(_._1.equalsIgnoreCase(name)).map(_._2)

            override def getParameter(name: String): String = newParameters.find(_._1.equalsIgnoreCase(name)).map(_._2).orNull


            override def getMethod = method.toUpperCase

            override def getHeader(name: String): String = {
              name.toLowerCase match {
                case "content-length" => getContentLength.toString
                case _ => getNewParameter(name).getOrElse(super.getHeader(name))
              }
            }

            override def getHeaderNames: java.util.Enumeration[String] = {
              (super.getHeaderNames.asScala ++ newParameters.keys).toSeq.distinct.iterator.asJavaEnumeration
            }

            override def getParameterMap: java.util.Map[String, Array[String]] = {
              newParameters.map(p => (p._1, Array(p._2))).asJava
            }

            override def getContentType: String = {
              getHeader(ContentType)
            }

            override def getContentLength: Int = {
              getNewParameter(Content).map(_.length).getOrElse(0)
            }


            override def getInputStream = {
              val content = getNewParameter(Content).getOrElse("")

              val byteArrayInputStream = new ByteArrayInputStream(content.getBytes(CharEncoding.UTF_8))
              new DelegatingServletInputStream(byteArrayInputStream)
            }

            override def getQueryString: String = {
              val newParametersString = newParameters
                .filterNot(p => TincanHeaders.contains(p._1.toLowerCase))
                .map(p => p._1 + "=" + URLEncoder.encode(p._2, enc))
                .mkString("&")

              val srcString = super.getQueryString

              if (srcString.isEmpty) newParametersString
              else srcString + "&" + newParametersString
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

