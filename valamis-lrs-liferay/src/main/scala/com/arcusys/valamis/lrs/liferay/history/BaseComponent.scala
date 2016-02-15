package com.arcusys.valamis.lrs.liferay.history

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
  def string2bool(l: String) = Try { l.toInt != 0 } match {
    case Success(r) => r
    case Failure(_) => l.toLowerCase match {
      case "true" | "t" => true
      case "false" => false
      case _ => false
    }
  }

  def getInteractionComponent(str: Option[String]): Seq[InteractionComponent] = Try {
    str
      .map { x => JsonHelper.fromJson[Seq[InteractionComponent]](x) }
      .getOrElse(Seq[InteractionComponent]())
  } getOrElse Seq()

  def getUUID(str: String) = UUID.fromString(
    str.replace("\"", "").replace("\'", "")
  )

  def getUUIDOrRandom(str: Option[String]) = str match {
    case None => UUID.randomUUID()
    case Some(v) => UUID.fromString(v.replace("\"", ""))
  }

  def getUUIDOption(str: Option[String]): Option[UUID] = str match {
    case None => None
    case Some(v) => Some(UUID.fromString(v.replace("\"", "")))
  }

  def getDateTimeOption(dt: Option[Date]): Option[DateTime] = dt.map { x => new DateTime(x) }

  def getDateTimeOrNow(dt: Option[Date]): DateTime = getDateTimeOption(dt).getOrElse(DateTime.now)

  def getScore(score: Option[String]) = score match {
    case None => None
    case Some(v) if v.isEmpty => None
    case Some(v) if !v.isEmpty =>  Some(JsonHelper.fromJson[Score](v, ScoreSerializer))
  }

  def getExtensions(ex: Option[String]): Option[ExtensionMap] = ex match {
    case None => Some(ExtensionMap())
    case Some(v) if v.isEmpty || v.equals("[]") => Some(ExtensionMap())
    case Some(v) => JsonHelper.fromJson[Option[ExtensionMap]](v)
  }

  def getLanguageMap(ex: String) = if(ex.isEmpty) Some(LanguageMap())
  else Some(JsonHelper.fromJson[LanguageMap](ex))

  def getLanguageMapOption(ex: Option[String]): Option[LanguageMap] = ex match {
    case None => Some(LanguageMap())
    case Some(v) if v.isEmpty || v.equals("[]") => Some(LanguageMap())
    case Some(v) => Some(JsonHelper.fromJson[LanguageMap](v))
  }

  def getLanguageMap(ex: Option[String]): LanguageMap = ex match {
    case None => LanguageMap()
    case Some(v) if v.isEmpty || v.equals("[]") => LanguageMap()
    case Some(v) => JsonHelper.fromJson[LanguageMap](v)
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
