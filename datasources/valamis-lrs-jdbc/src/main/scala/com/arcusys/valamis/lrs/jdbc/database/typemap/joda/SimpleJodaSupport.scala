package com.arcusys.valamis.lrs.jdbc.database.typemap.joda

import javax.inject.Inject

import scala.slick.driver.JdbcDriver

/**
 * Created by Iliya Tryapitsin on 03.08.15.
 */
class SimpleJodaSupport @Inject() (val driver: JdbcDriver) extends JodaSupport {
  protected val localDateTimeMapperDelegate = new JodaLocalDateTimeMapper(driver)
  protected val dateTimeZoneMapperDelegate  = new JodaDateTimeZoneMapper (driver)
  protected val localDateMapperDelegate     = new JodaLocalDateMapper    (driver)
  protected val localTimeMapperDelegate     = new JodaLocalTimeMapper    (driver)
  protected val dateTimeMapperDelegate      = new JodaDateTimeMapper     (driver)
  protected val instantMapperDelegate       = new JodaInstantMapper      (driver)
}

