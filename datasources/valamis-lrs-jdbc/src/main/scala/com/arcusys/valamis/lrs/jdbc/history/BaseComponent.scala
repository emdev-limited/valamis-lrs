package com.arcusys.valamis.lrs.jdbc.history

import java.sql.Date
import java.util.UUID

import com.arcusys.valamis.lrs.serializer.ScoreSerializer
import com.arcusys.valamis.lrs.tincan._
import com.arcusys.json.JsonHelper
import org.joda.time.DateTime

import scala.util._

/**
 * Created by Iliya Tryapitsin on 24/03/15.
 */


object Helper {

  def string2bool(l: String) =
    Try { l.toInt != 0 } recover { case f =>
      l.toLowerCase match {
        case "true" | "t" => true
        case _ => false
      }
    } get


  def getInteractionComponent(str: Option[String]): Seq[InteractionComponent] = Try {
    str
      .map { x => JsonHelper.fromJson[Seq[InteractionComponent]](x) }
      .getOrElse(Seq[InteractionComponent]())
  } getOrElse Seq()

  def getUUID(str: String) = UUID.fromString(
    str.replace("\"", "").replace("\'", "")
  )

  def getUUIDOrRandom(str: Option[String]) =
    str.fold(UUID.randomUUID)(s => UUID.fromString(s.replace("\"","")))

  def getUUIDOption(str: Option[String]): Option[UUID] = str match {
    case None => None
    case Some(v) => Some(UUID.fromString(v.replace("\"", "")))
  }

  def getDateTimeOption(dt: Option[Date]): Option[DateTime] = dt.map { x => new DateTime(x) }

  def getDateTimeOrNow(dt: Option[Date]): DateTime = getDateTimeOption(dt).getOrElse(DateTime.now)

  def getScore(os: Option[String]) = os match {
    case Some(v) if v.nonEmpty => Some(JsonHelper.fromJson[Score](v, ScoreSerializer))
    case _ => None
  }

  def getExtensions(ex: Option[String]): Option[ExtensionMap] = ex match {
    case Some(v) if v.nonEmpty && !v.equals("[]") => JsonHelper.fromJson[Option[ExtensionMap]](v)
    case _ => Some(ExtensionMap())
  }

  def getLanguageMap(ex: String) = if(ex.isEmpty) Some(LanguageMap())
  else Some(JsonHelper.fromJson[LanguageMap](ex))

  def getLanguageMapOption(ex: Option[String]): Option[LanguageMap] = ex match {
    case Some(v) if v.nonEmpty && !v.equals("[]") => Some(JsonHelper.fromJson[LanguageMap](v))
    case _ => Some(ExtensionMap())
  }

  def getLanguageMap(ex: Option[String]): LanguageMap = ex match {
    case Some(v) if v.nonEmpty && !v.equals("[]") => JsonHelper.fromJson[LanguageMap](v)
    case _ => LanguageMap()
  }

  def getVerb(id: Option[String], display: String) = {
    require(id.isDefined,     "Verb Id should be defined")
    require(!display.isEmpty, "Verb Display should not empty")

    Verb(id.get, JsonHelper.fromJson[LanguageMap](display))
  }

  def getVerb(id: Option[String], display: Option[String]) = {
    require(id.isDefined,     "Verb Id should be defined")

    Verb(id.get, getLanguageMap(display))
  }
}

trait BaseComponent {

  protected def getTableName(name: String) = name
}
