package com.arcusys.valamis.lrs.jdbc.database.typemap

import com.arcusys.valamis.lrs.jdbc.database.typemap.joda.{ResultGetter, ParameterSetter}

import scala.slick.driver.JdbcDriver

/**
 * Created by Iliya Tryapitsin on 03.08.15.
 */

trait CustomTypeMapper[SourceT, DestT] {
  val driver: JdbcDriver

  val typeMapper:   driver.DriverJdbcType  [SourceT]
  val getResult:    ResultGetter   [DestT, SourceT]
  val setParameter: ParameterSetter[DestT, SourceT]
}
