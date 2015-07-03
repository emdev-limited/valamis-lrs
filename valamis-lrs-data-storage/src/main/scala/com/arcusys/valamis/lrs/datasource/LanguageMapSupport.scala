package com.arcusys.valamis.lrs.datasource

import java.sql.{ResultSet, PreparedStatement}
import scala.slick.driver.JdbcProfile
import scala.slick.jdbc._
import com.arcusys.valamis.lrs.tincan.LanguageMap
import com.arcusys.valamis.utils.serialization.JsonHelper
import com.github.tototoshi.slick.converter.SqlTypeConverter

/**
 * Created by Iliya Tryapitsin on 17.06.15.
 */
trait LanguageMapStringConverter
  extends SqlTypeConverter[String, LanguageMap] {

  def toSqlType(z: LanguageMap): String =
    if (z == null || z.isEmpty) null
    else JsonHelper.toJson(z)

  def fromSqlType(z: String): LanguageMap =
    if (z == null) Map[String, String]()
    else JsonHelper.fromJson[LanguageMap](z)
}

class LanguageMapSupport(val driver: JdbcProfile) {

  object TypeMapper
    extends driver.DriverJdbcType[LanguageMap]
    with LanguageMapStringConverter {

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

  object LanguageMapGetResult
    extends ValamisGetResult[String, LanguageMap]
    with LanguageMapStringConverter {

    def next      (rs: PositionedResult): String         = rs.nextString()
    def nextOption(rs: PositionedResult): Option[String] = rs.nextStringOption()
  }

  object LanguageMapSetParameter
    extends ValamisSetParameter[String, LanguageMap]
    with LanguageMapStringConverter {

    def set      (rs: PositionedParameters, z: String): Unit         = rs.setString(z)
    def setOption(rs: PositionedParameters, z: Option[String]): Unit = rs.setStringOption(z)
  }
}




