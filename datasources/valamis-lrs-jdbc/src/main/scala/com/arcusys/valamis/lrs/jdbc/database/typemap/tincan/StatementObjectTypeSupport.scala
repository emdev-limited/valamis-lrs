package com.arcusys.valamis.lrs.jdbc.database.typemap.tincan

import java.sql.{PreparedStatement, ResultSet}

import com.arcusys.valamis.lrs.jdbc.database.typemap.CustomTypeMapper
import com.arcusys.valamis.lrs.jdbc.database.typemap.joda.converter.SqlTypeConverter
import com.arcusys.valamis.lrs.jdbc.database.typemap.joda.{ResultGetter, ParameterSetter}
import com.arcusys.valamis.lrs.tincan._

import scala.slick.driver.{JdbcDriver, JdbcProfile}
import scala.slick.jdbc._

/**
 * Created by Iliya Tryapitsin on 17.06.15.
 */
trait StatementObjectTypeStringConverter
  extends SqlTypeConverter[String, StatementObjectType.Type] {

  def toSqlType(z: StatementObjectType.Type): String = z.toString

  def fromSqlType(z: String): StatementObjectType.Type = StatementObjectType.withName(z)
}

class StatementObjectTypeSupport(val driver: JdbcDriver)
  extends CustomTypeMapper[StatementObjectType.Type, String] {

  import driver._

  override val typeMapper =
    new DriverJdbcType[StatementObjectType.Type] with StatementObjectTypeStringConverter {

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

  override val getResult =
    new ResultGetter[String, StatementObjectType.Type] with StatementObjectTypeStringConverter {

      def next      (rs: PositionedResult): String         = rs.nextString()
      def nextOption(rs: PositionedResult): Option[String] = rs.nextStringOption()
    }

  override val setParameter =
    new ParameterSetter[String, StatementObjectType.Type] with StatementObjectTypeStringConverter {

      def set      (rs: PositionedParameters, z: String): Unit         = rs.setString(z)
      def setOption(rs: PositionedParameters, z: Option[String]): Unit = rs.setStringOption(z)
    }
}




