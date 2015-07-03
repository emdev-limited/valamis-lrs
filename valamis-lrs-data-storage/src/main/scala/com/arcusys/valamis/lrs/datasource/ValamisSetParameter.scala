package com.arcusys.valamis.lrs.datasource

import com.github.tototoshi.slick.converter.SqlTypeConverter

import scala.slick.jdbc.{SetParameter, PositionedParameters}

/**
 * Created by Iliya Tryapitsin on 17.06.15.
 */
trait ValamisSetParameter[A, B] {
  self: SqlTypeConverter[A, B] =>

  def set(rs: PositionedParameters, d: A): Unit

  def setOption(rs: PositionedParameters, d: Option[A]): Unit

  object setJodaParameter extends SetParameter[B] {
    def apply(d: B, p: PositionedParameters) {
      set(p, toSqlType(d))
    }
  }

  object setJodaOptionParameter extends SetParameter[Option[B]] {
    def apply(d: Option[B], p: PositionedParameters) {
      setOption(p, d.map(toSqlType))
    }
  }
}
