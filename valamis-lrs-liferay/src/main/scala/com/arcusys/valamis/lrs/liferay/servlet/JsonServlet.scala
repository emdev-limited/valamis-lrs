package com.arcusys.valamis.lrs.liferay.servlet

import javax.servlet.http.{HttpServletResponse, HttpServletRequest}

import com.arcusys.json.{JsonDeserializeException, JsonHelper}
import com.arcusys.valamis.lrs.exception._
import com.arcusys.valamis.lrs.liferay._
import com.arcusys.valamis.lrs.liferay.exception._
import com.arcusys.valamis.lrs.liferay.servlet.request.BaseLrsRequest
import com.arcusys.valamis.lrs.serializer._
import org.joda.time.DateTime
import org.json4s.DefaultFormats

import scala.util._

/**
 * Created by Iliya Tryapitsin on 29.04.15.
 */
trait JsonServlet {
  this: BaseLrsServlet with Loggable =>

  implicit val formats = DefaultFormats
  protected lazy val serializers = tincanSerializers()

  def jsonAction[T <: BaseLrsRequest](action: (T) => Any,
                                      request: HttpServletRequest,
                                      response: HttpServletResponse)(implicit manifest: Manifest[T]): Unit = {
    val time = requestsTimer.map(_.time)

    try {
      val r = manifest.runtimeClass
        .getConstructor(classOf[HttpServletRequest])
        .newInstance(request)

      setJsonHeaders(request, response)
      tryAction({
        val model = r.asInstanceOf[T]
        action(model) match {
          case result if result == Unit => response.setStatus(HttpServletResponse.SC_NO_CONTENT)

          case result if response.getContentType.startsWith("application/octet-stream") =>
            response.setStatus(HttpServletResponse.SC_OK)
            response.getOutputStream.flush()
            response.getOutputStream.close()

          case result: String =>
            response.setStatus(HttpServletResponse.SC_OK)
            response.setHeader("Content-Type", "text/html; charset=UTF-8")
            response.getWriter.write(result)
            response.getWriter.flush()
            response.getWriter.close()
            logger.debug(s"Response to ${request.getPathInfo}: $result")

          case result: AnyRef =>
            val json = JsonHelper.toJson(result, serializers: _*)
            response.setStatus(HttpServletResponse.SC_OK)
            response.getWriter.write(json)
            response.getWriter.flush()
            response.getWriter.close()
            logger.debug(s"Response to ${request.getPathInfo}: $result")
        }
      }, request, response)

    } finally { time.foreach(_.stop) }

  }

  def jsonAction(action: () => Any,
                 request: HttpServletRequest,
                 response: HttpServletResponse): Unit = {

    setJsonHeaders(request, response)
    tryAction({

      action() match {
        case result if result == Unit => response.setStatus(HttpServletResponse.SC_NO_CONTENT)
        case result if response.getContentType.startsWith("application/octet-stream") =>
          response.setStatus(HttpServletResponse.SC_OK)
          response.getOutputStream.flush()
          response.getOutputStream.close()

        case result: String =>
          response.setStatus(HttpServletResponse.SC_OK)
          response.setHeader(ContentType, "text/html; charset=UTF-8")
          response.getWriter.write(result)
          response.getWriter.flush()
          response.getWriter.close()
          logger.debug(s"Response to ${request.getPathInfo}: $result")

        case result: AnyRef =>
          val json = JsonHelper.toJson(result, serializers: _*)
          response.setStatus(HttpServletResponse.SC_OK)
          response.getWriter.write(json)
          response.getWriter.flush()
          response.getWriter.close()
          logger.debug(s"Response to ${request.getPathInfo}: $result")
      }
    }, request, response)
  }

  protected def setJsonHeaders(request: HttpServletRequest,
                               response: HttpServletResponse): Unit = {

    val userAgent = request.getHeader(UserAgent)
    if (userAgent != null && (userAgent.contains("MSIE 9") || userAgent.contains("MSIE 8"))) //Because IE with versions below 10 doesn't support application/json
      response.setHeader(ContentType, "text/html; charset=UTF-8")
    else
      response.setHeader(ContentType, "application/json; charset=UTF-8")
  }

  def tryAction(action: => Unit,
                request: HttpServletRequest,
                response: HttpServletResponse): Unit = {
    val startDT = DateTime.now()

    logger.debug("***************************************")
    logger.debug(s"${request.getMethod} - ${request.getRequestURL.toString}")

    setHeaders(response)
    Try(action) match {
      case Success(_) =>
      case Failure(ex) =>
        ex match {
          // 400
          case ex: VerbInvalidException                => errorResponse(response, HttpServletResponse.SC_BAD_REQUEST, ex.getMessage)
          case ex: JsonDeserializeException            => errorResponse(response, HttpServletResponse.SC_BAD_REQUEST, ex.getMessage)
          case ex: IllegalArgumentException            => errorResponse(response, HttpServletResponse.SC_BAD_REQUEST, ex.getMessage)
          case ex: InvalidOrMissingArgumentException   => errorResponse(response, HttpServletResponse.SC_BAD_REQUEST, ex.getMessage)
          case ex: IncorrectVoidedStatementException   => errorResponse(response, HttpServletResponse.SC_BAD_REQUEST, ex.getMessage)
          case ex: DocumentInvalidContentTypeException => errorResponse(response, HttpServletResponse.SC_BAD_REQUEST, ex.getMessage)
          // 401
          case ex: UnauthorizedException   => errorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, ex.getMessage)
          // 403
          case ex: ForbiddenException      => errorResponse(response, HttpServletResponse.SC_FORBIDDEN,    ex.getMessage)
          // 404
          case ex: NotFoundException       => errorResponse(response, HttpServletResponse.SC_NOT_FOUND, ex.getMessage)
          case ex: RecordNotFoundException => errorResponse(response, HttpServletResponse.SC_NOT_FOUND, ex.getMessage)
          // 409
          case ex: ConflictEntityException => errorResponse(response, HttpServletResponse.SC_CONFLICT, ex.getMessage)
          //TODO: 412
          // 413
          case ex: RequestEntityTooLargeException => errorResponse(response, HttpServletResponse.SC_REQUEST_ENTITY_TOO_LARGE, ex.getMessage)
          // 500
          case ex: Exception =>
            errorResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ex.getMessage)
            logger.error(ex)
        }
    }

    logger.debug(s"Started at: ${startDT} Finished at: ${DateTime.now()}")
    logger.debug(s"Delta handle request: ${DateTime.now.minus(startDT.getMillis).getMillis} ms.")
    logger.debug("***************************************")
  }

  protected def errorResponse(response: HttpServletResponse, code: Int, message: String) = {
    response.setStatus(code)
    val writer = response.getWriter
    if (message != null && message.nonEmpty && writer != null) {
      writer.write(message)
      writer.flush()
      writer.close()
    }
  }

}
