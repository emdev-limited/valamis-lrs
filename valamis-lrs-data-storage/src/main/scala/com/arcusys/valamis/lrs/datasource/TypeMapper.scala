package com.arcusys.valamis.lrs.datasource

/**
 * Created by Iliya Tryapitsin on 04/01/15.
 */

import java.sql._
import com.arcusys.valamis.lrs.datasource.row.ContextActivityType
import com.arcusys.valamis.lrs.tincan._
import com.arcusys.valamis.utils.Version
import com.arcusys.valamis.utils.serialization.JsonHelper
import org.joda.time._
import scala.slick.driver.JdbcDriver.simple._

/**
 * Custom Type mappers for Slick.
 */
trait TypeMapper {


  /** Type mapper for [[org.joda.time.DateTime]] */
  implicit val dateTimeMapper: BaseColumnType[DateTime] = MappedColumnType.base[DateTime, Timestamp](
    dt => new Timestamp(dt.getMillis),
    ts => new DateTime(ts.getTime)
  )

  /** Type mapper for [[org.joda.time.LocalDate]] */
  implicit val localDateMapper: BaseColumnType[LocalDate] = MappedColumnType.base[LocalDate, Date](
    dt => new Date(dt.toDate.getTime),
    d => new LocalDate(d.getTime)
  )

  /** Type mapper for [[org.joda.time.Duration]] */
  implicit val durationTypeMapper: BaseColumnType[Duration] = MappedColumnType.base[Duration, Long](
    d => d.getMillis,
    l => Duration.millis(l)
  )

//  implicit val statementObjectTypeMapper = MappedColumnType.base[StatementObjectType.Type, String](
//    v => v.toString,
//    s => StatementObjectType.withName(s)
//  )

  implicit val interactionTypeMapper = MappedColumnType.base[InteractionType.Type, String](
    v => v.toString,
    s => InteractionType.withName(s)
  )

  implicit val tincanVersionTypeMapper = MappedColumnType.base[TincanVersion.Type, String](
    v => v.toString,
    s => TincanVersion.withName(s)
  )

  implicit val contextActivityTypeMapper = MappedColumnType.base[ContextActivityType.Type, String](
    v => v.toString,
    s => ContextActivityType.withName(s)
  )

  implicit val contentTypeMapper = MappedColumnType.base[ContentType.Type, String](
    v => v.toString,
    s => ContentType.withName(s)
  )

  implicit val seqMapper = MappedColumnType.base[Seq[String], String](
    seq => JsonHelper.toJson(seq),
    str => JsonHelper.fromJson[Seq[String]](str)
  )

  implicit val seqInteractionComponentMapper = MappedColumnType.base[Seq[InteractionComponent], String](
    seq => JsonHelper.toJson(seq),
    str => if (str.isEmpty)
      Seq[InteractionComponent]()
    else
      JsonHelper.fromJson[Seq[InteractionComponent]](str)
  )

//  implicit val stringStringMapMapper = MappedColumnType.base[StringStringMap, String](
//    seq => JsonHelper.toJson(seq),
//    str => if (str.isEmpty)
//      Map[String, String]()
//    else
//      JsonHelper.fromJson[StringStringMap](str)
//  )

  implicit val versionStringMapping = MappedColumnType.base[Version, String](
    ver => ver.toString,
    str => Version(str)
  )
}



