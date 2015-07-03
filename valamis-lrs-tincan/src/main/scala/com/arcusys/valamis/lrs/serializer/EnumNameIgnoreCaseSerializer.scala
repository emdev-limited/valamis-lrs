package com.arcusys.valamis.lrs.serializer

import org.json4s._

import scala.reflect.ClassTag

/**
 * Created by Iliya Tryapitsin on 25/02/15.
 */
class EnumNameIgnoreCaseSerializer[E <: Enumeration: ClassTag](enum: E)
  extends Serializer[E#Value] {
  import JsonDSL._

  val EnumerationClass = classOf[E#Value]

  def deserialize(implicit format: Formats):  PartialFunction[(TypeInfo, JValue), E#Value] = {
    case (t @ TypeInfo(EnumerationClass, _), json) if isValid(json) => {
      json match {
        case JString(value) => enum.withName(value.toLowerCase)
        case value => throw new MappingException(s"Can't convert $value to $EnumerationClass")
      }
    }
  }

  private[this] def isValid(json: JValue) = json match {
    case JString(value) if enum.values.exists(x => x.toString.equalsIgnoreCase(value)) => true
    case _ => false
  }

  def serialize(implicit format: Formats): PartialFunction[Any, JValue] = {
    case i: E#Value => i.toString.capitalize
  }
}

object EnumNameIgnoreCaseSerializer {
  def apply[E <: Enumeration: ClassTag](enum: E) = new EnumNameIgnoreCaseSerializer(enum)
}