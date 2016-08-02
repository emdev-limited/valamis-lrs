package com.arcusys.valamis.lrs.liferay

import com.arcusys.learn.liferay.LiferayClasses.{LCompany, LDuplicateColumnNameException, LDuplicateTableNameException}
import com.arcusys.learn.liferay.services.{ClassNameLocalServiceHelper, CompanyLocalServiceHelper, ExpandoLocalServiceHelper}
import com.arcusys.learn.liferay.util.PortalUtilHelper
import com.arcusys.valamis.lrs._

import scala.util.{Failure, Success, Try}

/**
  * Created by iliyatryapitsin on 12/11/15.
  */
trait LrsModeLocator extends Loggable {
  lazy private val defaultCompanyId    = PortalUtilHelper.getDefaultCompanyId
  lazy private val company             = CompanyLocalServiceHelper.getCompany(defaultCompanyId)
  lazy private val classNameId         = ClassNameLocalServiceHelper.getClassNameId(companyClassName)
  lazy private val companyClassName    = classOf[LCompany].getName

  private val modeColumnName = "VALAMIS_LRS_MODE"

  /**
    * Return LRS running mode
    * @return
    */
  def getLrsMode: RunningMode.Type = {

    val table  = ExpandoLocalServiceHelper .getDefaultTable(company.getCompanyId, companyClassName)
    val column = ExpandoLocalServiceHelper.getColumn      (table.getTableId,     modeColumnName)
    val value  = ExpandoLocalServiceHelper .getData(
      company.getCompanyId,
      companyClassName,
      table.getName,
      column.getName,
      classNameId,
      RunningMode.Default.toString
    )

    RunningMode.withName(value toString)
  }

  /**
    * Save LRS mode to Liferay's custom field
    * @param mode
    * @return
    */
  def saveLrsModeSettings(mode: String) = {
    val table  = ExpandoLocalServiceHelper.getDefaultTable(company.getCompanyId, companyClassName)
    val lrsModeCol = ExpandoLocalServiceHelper.getColumn(table.getTableId,      modeColumnName)

    ExpandoLocalServiceHelper.deleteValue(company.getCompanyId, companyClassName, table.getName, lrsModeCol.getName, classNameId)
    ExpandoLocalServiceHelper.addValue   (company.getCompanyId, companyClassName, table.getName, lrsModeCol.getName, classNameId, mode)

    logger.info(s"Current mode is: $mode")
  }

  def initLrsModeSettings(): Unit = {

    val table = Try {
      ExpandoLocalServiceHelper.addDefaultTable(company.getCompanyId, companyClassName)
    } match {
      case Failure(e: LDuplicateTableNameException) =>
        logger.info(s"Table '$companyClassName' exists already")
        ExpandoLocalServiceHelper.getDefaultTable(company.getCompanyId, companyClassName)

      case Success(e) =>
        logger.info(s"Created '$companyClassName' table")
        e
    }

    Try {
      ExpandoLocalServiceHelper.addColumn(table.getTableId, modeColumnName, ExpandoLocalServiceHelper.Strings)
    } match {
      case Failure(e: LDuplicateColumnNameException) =>
        logger.info(s"Column '$modeColumnName' exists already")

      case Success(e) =>
        logger.info(s"Created '$modeColumnName' column")
    }
  }

}
