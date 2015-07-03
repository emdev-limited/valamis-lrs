package com.arcusys.valamis.lrs.datasource

import java.sql.{ResultSet, PreparedStatement}
import scala.slick.driver.JdbcProfile
import scala.slick.jdbc._
import com.arcusys.valamis.lrs.tincan._
import com.github.tototoshi.slick.converter.SqlTypeConverter

/**
 * Created by Iliya Tryapitsin on 17.06.15.
 */
trait StatementObjectTypeStringConverter
  extends SqlTypeConverter[String, StatementObjectType.Type] {

  def toSqlType(z: StatementObjectType.Type): String = z.toString

  def fromSqlType(z: String): StatementObjectType.Type = StatementObjectType.withName(z)
}

class StatementObjectTypeSupport(val driver: JdbcProfile) {

  object TypeMapper
    extends driver.DriverJdbcType[StatementObjectType.Type]
    with StatementObjectTypeStringConverter {

      def sqlType = java.sql.Types.VARCHAR

      override def setValue(v: StatementObjectType.Type,
                            p: PreparedStatement,
                            idx: Int): Unit = p.setString(idx, toSqlType(v))

      override def getValue(r: ResultSet,
                            idx: Int): StatementObjectType.Type = fromSqlType(r.getString(idx))

      override def updateValue(v: StatementObjectType.Type,
                               r: ResultSet,
                               idx: Int): Unit = r.updateString(idx, toSqlType(v))

      override def valueToSQLLiteral(value: StatementObjectType.Type) = s"""'${toSqlType(value)}'"""
    }

  object StatementObjectTypeGetResult
    extends ValamisGetResult[String, StatementObjectType.Type]
    with StatementObjectTypeStringConverter {

    def next      (rs: PositionedResult): String         = rs.nextString()
    def nextOption(rs: PositionedResult): Option[String] = rs.nextStringOption()
  }

  object StatementObjectTypeSetParameter
    extends ValamisSetParameter[String, StatementObjectType.Type]
    with StatementObjectTypeStringConverter {

    def set      (rs: PositionedParameters, z: String): Unit         = rs.setString(z)
    def setOption(rs: PositionedParameters, z: Option[String]): Unit = rs.setStringOption(z)
  }
}




