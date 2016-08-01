/*
 * Copyright 2013 Toshiyuki Takahashi
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *     * Redistributions of source code must retain the above copyright notice,
 *       this list of conditions and the following disclaimer.
 *
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

package com.arcusys.valamis.lrs.jdbc.database.typemap.joda

import java.sql._
import com.arcusys.valamis.lrs.jdbc.database.typemap.CustomTypeMapper
import com.arcusys.valamis.lrs.jdbc.database.typemap.joda.converter._

import scala.slick.driver.{MySQLDriver, JdbcDriver}
import org.joda.time._
import scala.slick.jdbc.{ PositionedResult, PositionedParameters }

class JodaDateTimeZoneMapper(val driver: JdbcDriver) extends CustomTypeMapper[DateTimeZone, String] {

  import driver._

  override val typeMapper =
    new DriverJdbcType[DateTimeZone] with JodaDateTimeZoneSqlStringConverter {
      def sqlType = java.sql.Types.VARCHAR
      override def setValue(v: DateTimeZone, p: PreparedStatement, idx: Int): Unit =
        p.setString(idx, toSqlType(v))
      override def getValue(r: ResultSet, idx: Int): DateTimeZone =
        fromSqlType(r.getString(idx))
      override def updateValue(v: DateTimeZone, r: ResultSet, idx: Int): Unit =
        r.updateString(idx, toSqlType(v))
      override def valueToSQLLiteral(value: DateTimeZone) = toSqlType(value)
    }

  override val setParameter =
    new ParameterSetter[String, DateTimeZone] with JodaDateTimeZoneSqlStringConverter {
      def set(rs: PositionedParameters, z: String): Unit = rs.setString(z)
      def setOption(rs: PositionedParameters, z: Option[String]): Unit = rs.setStringOption(z)
    }

  override val getResult =
    new ResultGetter[String, DateTimeZone] with JodaDateTimeZoneSqlStringConverter {
      def next(rs: PositionedResult): String = rs.nextString()
      def nextOption(rs: PositionedResult): Option[String] = rs.nextStringOption()
    }
}

class JodaLocalDateMapper(val driver: JdbcDriver) extends CustomTypeMapper[LocalDate, Date] {

  import driver._

  override val typeMapper =
    new DriverJdbcType[LocalDate] with JodaLocalDateSqlDateConverter {
      def zero = new LocalDate(0L)
      def sqlType = Types.DATE
      override def setValue(v: LocalDate, p: PreparedStatement, idx: Int): Unit =
        p.setDate(idx, toSqlType(v))
      override def getValue(r: ResultSet, idx: Int): LocalDate =
        fromSqlType(r.getDate(idx))
      override def updateValue(v: LocalDate, r: ResultSet, idx: Int): Unit =
        r.updateDate(idx, toSqlType(v))
      override def valueToSQLLiteral(value: LocalDate) = "{d '" + toSqlType(value).toString + "'}"
    }

  override val getResult =
    new ResultGetter[Date, LocalDate] with JodaLocalDateSqlDateConverter {
      def next(rs: PositionedResult): Date = rs.nextDate()
      def nextOption(rs: PositionedResult): Option[Date] = rs.nextDateOption()
    }

  override val setParameter =
    new ParameterSetter[Date, LocalDate] with JodaLocalDateSqlDateConverter {
      def set(rs: PositionedParameters, d: Date): Unit = rs.setDate(d)
      def setOption(rs: PositionedParameters, d: Option[Date]): Unit = rs.setDateOption(d)
    }

}

class JodaDateTimeMapper(val driver: JdbcDriver) extends CustomTypeMapper[DateTime, Timestamp] {

  import driver._

  override val typeMapper =
    new DriverJdbcType[DateTime] with JodaDateTimeSqlTimestampConverter {
      def zero = new DateTime(0L)
      def sqlType = java.sql.Types.TIMESTAMP
      override def sqlTypeName =  driver match {
        case driver: MySQLDriver => "DATETIME"
        case _ => columnTypes.timestampJdbcType.sqlTypeName
      }
      override def setValue(v: DateTime, p: PreparedStatement, idx: Int): Unit =
        p.setTimestamp(idx, toSqlType(v))
      override def getValue(r: ResultSet, idx: Int): DateTime =
        fromSqlType(r.getTimestamp(idx))
      override def updateValue(v: DateTime, r: ResultSet, idx: Int): Unit =
        r.updateTimestamp(idx, toSqlType(v))
      override def valueToSQLLiteral(value: DateTime) = columnTypes.timestampJdbcType.valueToSQLLiteral(toSqlType(value))
    }

  override val getResult =
    new ResultGetter[Timestamp, DateTime] with JodaDateTimeSqlTimestampConverter {
      def next(rs: PositionedResult): Timestamp = rs.nextTimestamp()
      def nextOption(rs: PositionedResult): Option[Timestamp] = rs.nextTimestampOption()
    }

  override val setParameter =
    new ParameterSetter[Timestamp, DateTime] with JodaDateTimeSqlTimestampConverter {
      def set(rs: PositionedParameters, d: Timestamp): Unit = rs.setTimestamp(d)
      def setOption(rs: PositionedParameters, d: Option[Timestamp]): Unit = rs.setTimestampOption(d)
    }
}

class JodaInstantMapper(val driver: JdbcDriver) extends CustomTypeMapper[Instant, Timestamp] {

  import driver._

  override val typeMapper =
    new DriverJdbcType[Instant] with JodaInstantSqlTimestampConverter {
      def zero = new DateTime(0L)
      def sqlType = java.sql.Types.TIMESTAMP
      override def setValue(v: Instant, p: PreparedStatement, idx: Int): Unit =
        p.setTimestamp(idx, toSqlType(v))
      override def getValue(r: ResultSet, idx: Int): Instant =
        fromSqlType(r.getTimestamp(idx))
      override def updateValue(v: Instant, r: ResultSet, idx: Int): Unit =
        r.updateTimestamp(idx, toSqlType(v))
      override def valueToSQLLiteral(value: Instant) = "{ts '" + toSqlType(value).toString + "'}"
    }

  override val getResult =
    new ResultGetter[Timestamp, Instant] with JodaInstantSqlTimestampConverter {
      def next(rs: PositionedResult): Timestamp = rs.nextTimestamp()
      def nextOption(rs: PositionedResult): Option[Timestamp] = rs.nextTimestampOption()
    }

  override val setParameter =
    new ParameterSetter[Timestamp, Instant] with JodaInstantSqlTimestampConverter {
      def set(rs: PositionedParameters, d: Timestamp): Unit = rs.setTimestamp(d)
      def setOption(rs: PositionedParameters, d: Option[Timestamp]): Unit = rs.setTimestampOption(d)
    }

}

class JodaLocalDateTimeMapper(val driver: JdbcDriver) extends CustomTypeMapper[LocalDateTime, Timestamp] {

  import driver._

  override val typeMapper =
    new DriverJdbcType[LocalDateTime] with JodaLocalDateTimeSqlTimestampConverter {
      def zero = new LocalDateTime(0L)
      def sqlType = java.sql.Types.TIMESTAMP
      override def sqlTypeName = columnTypes.timestampJdbcType.sqlTypeName
      override def setValue(v: LocalDateTime, p: PreparedStatement, idx: Int): Unit =
        p.setTimestamp(idx, toSqlType(v))
      override def getValue(r: ResultSet, idx: Int): LocalDateTime =
        fromSqlType(r.getTimestamp(idx))
      override def updateValue(v: LocalDateTime, r: ResultSet, idx: Int): Unit =
        r.updateTimestamp(idx, toSqlType(v))
      override def valueToSQLLiteral(value: LocalDateTime) = columnTypes.timestampJdbcType.valueToSQLLiteral(toSqlType(value))
    }

  override val getResult =
    new ResultGetter[Timestamp, LocalDateTime] with JodaLocalDateTimeSqlTimestampConverter {
      def next(rs: PositionedResult): Timestamp = rs.nextTimestamp()
      def nextOption(rs: PositionedResult): Option[Timestamp] = rs.nextTimestampOption()
    }

  override val setParameter =
    new ParameterSetter[Timestamp, LocalDateTime] with JodaLocalDateTimeSqlTimestampConverter {
      def set(rs: PositionedParameters, d: Timestamp): Unit = rs.setTimestamp(d)
      def setOption(rs: PositionedParameters, d: Option[Timestamp]): Unit = rs.setTimestampOption(d)
    }

}

class JodaLocalTimeMapper(val driver: JdbcDriver) extends CustomTypeMapper[LocalTime, Time] {

  import driver._

  override val typeMapper =
    new DriverJdbcType[LocalTime] with JodaLocalTimeSqlTimeConverter {
      def zero = new LocalTime(0L)
      def sqlType = java.sql.Types.TIME
      override def setValue(v: LocalTime, p: PreparedStatement, idx: Int): Unit =
        p.setTime(idx, toSqlType(v))
      override def getValue(r: ResultSet, idx: Int): LocalTime =
        fromSqlType(r.getTime(idx))
      override def updateValue(v: LocalTime, r: ResultSet, idx: Int): Unit =
        r.updateTime(idx, toSqlType(v))
      override def valueToSQLLiteral(value: LocalTime) = "{t '" + toSqlType(value).toString + "'}"
    }

  override val getResult =
    new ResultGetter[Time, LocalTime] with JodaLocalTimeSqlTimeConverter {
      def next(rs: PositionedResult): Time = rs.nextTime()
      def nextOption(rs: PositionedResult): Option[Time] = rs.nextTimeOption()
    }

  override val setParameter =
    new ParameterSetter[Time, LocalTime] with JodaLocalTimeSqlTimeConverter {
      def set(rs: PositionedParameters, d: Time): Unit = rs.setTime(d)
      def setOption(rs: PositionedParameters, d: Option[Time]): Unit = rs.setTimeOption(d)
    }

}

