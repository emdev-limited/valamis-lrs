package com.arcusys.valamis.lrs.serializer

import com.arcusys.valamis.lrs.exception.CanNotBeNullException
import com.arcusys.valamis.lrs.tincan.InteractionType
import org.json4s.CustomSerializer
import org.json4s.JsonAST.{JNull, JString}
import com.arcusys.valamis.lrs.tincan.Constants.InteractionType._

/**
 * Created by Iliya Tryapitsin on 29/12/14.
 */
object InteractionTypeSerializer extends CustomSerializer[InteractionType.Type](format => ( {
  case JString(value) =>
    if(value.isEmpty)
      throw new IllegalArgumentException

    InteractionType.withName(value.replace("_", "-").toLowerCase)

  case JNull => throw new CanNotBeNullException("interactionType value can not be NULL")
}, {
  case tpe: InteractionType.Type => JString(tpe.toString)
}))