package com.arcusys.valamis.lrs.jdbc.database.typemap.tincan

import java.sql.{PreparedStatement, ResultSet}

import com.arcusys.valamis.lrs.jdbc.database.typemap.joda.{ParameterSetter, ResultGetter}
import com.arcusys.valamis.lrs.jdbc.database.typemap.{CustomTypeMapper, JsonStringConverter}
import com.arcusys.valamis.lrs.tincan.LanguageMap

import scala.slick.driver.JdbcDriver
import scala.slick.jdbc._

class LanguageMapSupport(val driver: JdbcDriver)
  extends CustomTypeMapper[LanguageMap, String] {

  import driver._

  override val typeMapper =
    new DriverJdbcType[LanguageMap] with JsonStringConverter {

      def sqlType = java.sql.Types.VARCHAR

      override def setValue(v: LanguageMap,
                            p: PreparedStatement,
                            idx: Int): Unit = p.setString(idx, toSqlType(v))

      override def getValue(r: ResultSet,
                            idx: Int): LanguageMap = fromSqlType(r.getString(idx))

      override def updateValue(v: LanguageMap,
                               r: ResultSet,
                               idx: Int): Unit = r.updateString(idx, toSqlType(v))

      override def valueToSQLLiteral(value: LanguageMap) = toSqlType(value)
    }

  override val getResult =
    new ResultGetter[String, LanguageMap] with JsonStringConverter {

      def next      (rs: PositionedResult): String         = rs.nextString()
      def nextOption(rs: PositionedResult): Option[String] = rs.nextStringOption()
    }

  override val setParameter =
    new ParameterSetter[String, LanguageMap] with JsonStringConverter {

      def set      (rs: PositionedParameters, z: String): Unit         = rs.setString(z)
      def setOption(rs: PositionedParameters, z: Option[String]): Unit = rs.setStringOption(z)
    }
}




