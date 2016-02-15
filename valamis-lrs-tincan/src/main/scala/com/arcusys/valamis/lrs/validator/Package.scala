package com.arcusys.valamis.lrs

import java.util.UUID

import com.arcusys.valamis.lrs.exception.{IncorrectDateTimeFormatException, CanNotBeNullException, IncorrectUuidException}
import org.joda.time.DateTime
import org.json4s.JsonAST.{JNothing, JString}
import org.json4s.{Formats, JNull, JValue}

import scala.util.{Failure, Success, Try}

/**
 * Created by Iliya Tryapitsin on 01/04/15.
 */

package object validator {

  implicit class JValueExtensions(json: JValue) {

    def notNull = json match {
      case JNull => throw new CanNotBeNullException(json.toString)
      case _     => json
    }

    def getUuid(implicit format: Formats) = json match {
      case JNothing   => UUID.randomUUID
      case v: JString => Try {
        val raw = v.extract[String]
        if (raw.length != 36) throw new IllegalArgumentException

        UUID.fromString(raw)
      } match {
        case Success(u) => u
        case Failure(_) => throw new IncorrectUuidException("Incorrect UUID format")
      }
      case _            => throw new IncorrectUuidException("Incorrect UUID format")
    }


    def getUuidOption(implicit format: Formats) = json match {
      case JNothing   => None
      case v: JString => Try {
          val raw = v.extract[String]
          if (raw.length != 36) throw new IllegalArgumentException

          UUID.fromString(raw)
        } match {
          case Success(u) => Some(u)
          case Failure(_) => throw new IncorrectUuidException("Incorrect UUID format")
        }
      case _            => throw new IncorrectUuidException("Incorrect UUID format")
    }

    def getDateTimeOption(implicit format: Formats) = json match {
      case JNothing => None
      case v: JString => Try {
        v.extract[DateTime]
      } match {
        case Success(u) => Some(u)
        case Failure(_) => throw new IncorrectDateTimeFormatException
      }
      case _ => throw new IncorrectDateTimeFormatException
    }
  }

}
