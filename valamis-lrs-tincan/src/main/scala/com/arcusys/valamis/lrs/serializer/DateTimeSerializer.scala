package com.arcusys.valamis.lrs.serializer

import com.arcusys.valamis.lrs.exception.IncorrectDateTimeFormatException
import org.joda.time.DateTime
import org.joda.time.format.ISODateTimeFormat
import org.json4s.{CustomSerializer, JString}

import scala.util.{Failure, Success, Try}

/**
 * Created by Iliya Tryapitsin on 03/04/15.
 */
object DateTimeSerializer extends CustomSerializer[DateTime](implicit format => ( {
  case JString(str) => {
    Try {
      ISODateTimeFormat.dateTimeParser().parseDateTime(str)
    } match {
      case Success(dt) => dt
      case Failure(_) => throw new IncorrectDateTimeFormatException
    }
  }
}, {
  case dt: DateTime => JString(dt.toString)
}))
