package com.arcusys.valamis.lrs.auth.datasource

/**
 * Created by Iliya Tryapitsin on 04/01/15.
 */

import java.nio.ByteBuffer
import java.sql.{Date, Timestamp}

import com.arcusys.valamis.lrs.auth._
import com.arcusys.valamis.lrs.tincan.AuthorizationScope
import org.joda.time.{DateTime, Duration, LocalDate}

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

  /** Type mapper for [[AuthorizationScope.Type]] */
  implicit val scopeIntMapper: BaseColumnType[AuthorizationScope.ValueSet] = MappedColumnType.base[AuthorizationScope.ValueSet, Long](
    values => values.toBitMask.sum,
    bits   => AuthorizationScope.ValueSet.fromBitMask(Array(bits))
  )

  /** Type mapper for [[AuthenticationType.Type]] */
  implicit val authTypeStringMapper: BaseColumnType[AuthenticationType.Type] = MappedColumnType.base[AuthenticationType.Type, String](
    scope => scope.toString,
    str   => AuthenticationType.withName(str)
  )
}