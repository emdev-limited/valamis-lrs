package com.arcusys.valamis.lrs.liferay.servlet.request

import java.util.UUID
import javax.servlet.http.HttpServletRequest

import com.arcusys.valamis.lrs.liferay.exception
import com.arcusys.valamis.lrs.serializer.StatementSerializer
import com.arcusys.valamis.lrs.liferay.exception.InvalidOrMissingArgumentException
import com.arcusys.valamis.lrs.tincan.Statement
import com.arcusys.json.JsonHelper

import scala.collection.JavaConverters._
import scala.util.{Failure, Success, Try}

/**
 * Created by Iliya Tryapitsin on 29/12/14.
 */

class TincanActivityStateRequest(r: HttpServletRequest) extends BaseLrsRequest(r)
with BaseTincanActivityRequestComponent
with BaseTincanAgentRequestComponent {

  import TincanActivityStateRequest._

  def stateId = optional(STATE_ID)

  def hasStateId = has(STATE_ID)

  def registration: Option[UUID] = optional(REGISTRATION).map(UUID.fromString)

  def statement = Try(
    if (isMultipart) {
      if (request.getParameterMap.size == 0) {
        throw new InvalidOrMissingArgumentException("statement")
      }

      //TODO: not sure it is correct
      if (request.getParameterMap.size > 1) {
        throw new com.arcusys.valamis.lrs.liferay.exception.BadRequestException("More than one part of content with statement(s)")
      }

      val rawData = request.getParameterMap.asScala.head._2.asInstanceOf[Array[String]].head
      JsonHelper.fromJson[Statement](rawData, new StatementSerializer)
    } else {
      JsonHelper.fromJson[Statement](body, new StatementSerializer)
    }) match {
    case Success(value) => value
    case Failure(e) => throw new InvalidOrMissingArgumentException("statement")
  }
}

object TincanActivityStateRequest {
  val STATE_ID = "stateId"
  val REGISTRATION = "registration"

}