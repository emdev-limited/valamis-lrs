package com.arcusys.valamis.lrs.datasource

import com.github.tototoshi.slick.converter.SqlTypeConverter

import scala.slick.jdbc.{GetResult, PositionedResult}

/**
 * Created by Iliya Tryapitsin on 17.06.15.
 */
trait ValamisGetResult[A, B] {
  self: SqlTypeConverter[A, B] =>

  def next(rs: PositionedResult): A

  def nextOption(rs: PositionedResult): Option[A]

  object getResult extends GetResult[B] {
    def apply(rs: PositionedResult) = fromSqlType(next(rs))
  }

  object getOptionResult extends GetResult[Option[B]] {
    def apply(rs: PositionedResult) = nextOption(rs).map(fromSqlType)
  }
}
