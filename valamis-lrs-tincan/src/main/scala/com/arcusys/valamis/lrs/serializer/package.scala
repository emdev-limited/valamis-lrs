package com.arcusys.valamis.lrs

import com.arcusys.valamis.lrs.tincan._
import org.json4s.JsonAST.{JField, JString}
import org.json4s._
import Constants.Tincan.Field
import Constants.Tincan.Field._
import org.json4s.ext.UUIDSerializer
import scala.reflect.runtime.universe.{TypeTag, typeOf}

/**
 * Created by Iliya Tryapitsin on 02/04/15.
 */
package object serializer {
  def fieldTransformer[T : TypeTag]: scala.PartialFunction[org.json4s.JField, org.json4s.JField] = {
    case JField(Field.obj, jValue: JValue) =>
      JField(`object`, jValue)

    case JField(Field.`object`, jValue: JValue) =>
      JField(obj, jValue)

    case JField("jsonClass", jValue: JString) =>
      if (typeOf[T] =:= typeOf[StatementReference])
        JField(ObjectType, JString(StatementObjectType.statementReference.toString.capitalize))
      else
        JField(ObjectType, jValue)

    case JField("mBox",         jValue: JString) => JField(mBox,         jValue)
    case JField("mBoxSha1Sum",  jValue: JString) => JField(mBoxSha1Sum,  jValue)
    case JField("openId",       jValue: JString) => JField(openId,       jValue)
  }

  val removeSymbols = "\t\n\r ,"

  implicit class StringExtension(str: String) {
    def trimAll(symbols: String = removeSymbols): String = trimStart(0, symbols)

    @scala.annotation.tailrec
    final def trimStart(n: Int, removeSymbols: String = removeSymbols): String =
      if (n == str.length) ""
      else if (removeSymbols.indexOf(str.charAt(n)) < 0) trimEnd(n, str.length, removeSymbols)
      else trimStart(1 + n, removeSymbols)

    @scala.annotation.tailrec
    final def trimEnd(a: Int, n: Int, removeSymbols: String = removeSymbols): String =
      if (n <= a) str.substring(a, n)
      else if (removeSymbols.indexOf(str.charAt(n - 1)) < 0) str.substring(a, n)
      else trimEnd(a, n - 1, removeSymbols)
  }

  implicit class OptionStringExtension(val str: Option[String]) extends AnyVal {
    def trimAll(symbols: String = removeSymbols): Option[String] = str match {
      case None => None
      case Some(v) => v.trimAll(symbols).toOption
    }
  }

  def isStoredId = (field: (String, JValue)) => field._1.equalsIgnoreCase("storedId")

  def tincanSerializers(formatType: SerializeFormat = SerializeFormat()): Seq[Serializer[_]] = Seq(
     new StatementSerializer(formatType)
    ,new ContextSerializer(formatType)
    ,new ActorSerializer(formatType)
    ,new ActivitySerializer(formatType)
    ,new AgentSerializer(formatType)
    ,new GroupSerializer(formatType)
    ,new StatementObjectSerializer(formatType)
    ,new SubStatementSerializer(formatType)
    ,InteractionTypeSerializer
    ,StatementObjectTypeSerializer
    ,StatementReferenceSerializer
    ,ResultSerializer
    ,VerbSerializer
    ,DateTimeSerializer
    ,TincanVersionSerializer
    ,ScoreSerializer
    ,UUIDSerializer)


  def statementSerializers(formatType: SerializeFormat = SerializeFormat()): Formats = DefaultFormats +
    new ContextSerializer(formatType)         +
    new ActorSerializer(formatType)           +
    new StatementObjectSerializer(formatType) +
    InteractionTypeSerializer                 +
    ResultSerializer                          +
    VerbSerializer                            +
    DateTimeSerializer                        +
    TincanVersionSerializer                   +
    UUIDSerializer

  def actorSerializers(formatType: SerializeFormat = SerializeFormat()): Formats =  DefaultFormats +
    new AgentSerializer(formatType) +
    new GroupSerializer(formatType) +
    DateTimeSerializer

  def contextSerializers(implicit formatType: SerializeFormat = SerializeFormat()): Formats =  DefaultFormats +
    new ActorSerializer(formatType)                   +
    new GroupSerializer(formatType)                   +
    StatementReferenceSerializer                      +
    UUIDSerializer                                    +
    StatementObjectTypeSerializer                     +
    DateTimeSerializer

  def activitySerializer(formatType: SerializeFormat = SerializeFormat()): Formats = DefaultFormats +
    InteractionTypeSerializer

  def groupSerializers(formatType: SerializeFormat = SerializeFormat()): Formats =  DefaultFormats +
    new ActorSerializer(formatType)

  def subStatementSerializers(formatType: SerializeFormat = SerializeFormat()): Formats =  DefaultFormats +
    new StatementObjectSerializer(formatType) +
    new ActorSerializer(formatType)           +
    UUIDSerializer                            +
    VerbSerializer                            +
    DateTimeSerializer

  def statementRefSerializers: Formats =  DefaultFormats +
    ShortTypeHints(List(classOf[StatementReference])) +
    UUIDSerializer                                    +
    DateTimeSerializer

  def statementObjectSerializers(formatType: SerializeFormat = SerializeFormat()): Formats =  DefaultFormats +
    new ActorSerializer(formatType)           +
    UUIDSerializer                            +
    VerbSerializer                            +
    StatementReferenceSerializer              +
    new ActivitySerializer(formatType)        +
    new StatementObjectSerializer(formatType) +
    new SubStatementSerializer(formatType)    +
    DateTimeSerializer

  def resultSerializers: Formats = DefaultFormats +
    ScoreSerializer +
    DateTimeSerializer

}
