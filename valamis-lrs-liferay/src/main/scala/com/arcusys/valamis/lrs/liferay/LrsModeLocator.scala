package com.arcusys.valamis.lrs.liferay

import com.arcusys.valamis.lrs._
import com.liferay.portal.kernel.util.{PropsKeys, PropsUtil}
import com.liferay.portal.model.Company
import com.liferay.portal.service.{ClassNameLocalServiceUtil, CompanyLocalServiceUtil}
import com.liferay.portal.util.PortalUtil
import com.liferay.portlet.expando.{DuplicateColumnNameException, DuplicateTableNameException}
import com.liferay.portlet.expando.model.ExpandoColumnConstants
import com.liferay.portlet.expando.service.{ExpandoValueLocalServiceUtil, ExpandoColumnLocalServiceUtil, ExpandoTableLocalServiceUtil}

import scala.util.{Success, Failure, Try}

/**
  * Created by iliyatryapitsin on 12/11/15.
  */
trait LrsModeLocator extends Loggable {
  lazy private val defaultCompanyId    = PortalUtil.getDefaultCompanyId
  lazy private val company             = CompanyLocalServiceUtil.getCompany(defaultCompanyId)
  lazy private val classNameId         = ClassNameLocalServiceUtil.getClassNameId(companyClassName)
  lazy private val companyClassName    = classOf[Company].getName

  private val modeColumnName = "VALAMIS_LRS_MODE"

  /**
    * Return LRS running mode
    * @return
    */
  def getLrsMode: RunningMode.Type = {

    val table  = ExpandoTableLocalServiceUtil .getDefaultTable(company.getCompanyId, companyClassName)
    val column = ExpandoColumnLocalServiceUtil.getColumn      (table.getTableId,     modeColumnName)
    val value  = ExpandoValueLocalServiceUtil .getData(
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
    val table  = ExpandoTableLocalServiceUtil.getDefaultTable(company.getCompanyId, companyClassName)
    val lrsModeCol = ExpandoColumnLocalServiceUtil.getColumn(table.getTableId,      modeColumnName)

    ExpandoValueLocalServiceUtil.deleteValue(company.getCompanyId, companyClassName, table.getName, lrsModeCol.getName, classNameId)
    ExpandoValueLocalServiceUtil.addValue   (company.getCompanyId, companyClassName, table.getName, lrsModeCol.getName, classNameId, mode)

    logger.info(s"Current mode is: $mode")
  }

  def initLrsModeSettings(): Unit = {

    val table = Try {
      ExpandoTableLocalServiceUtil.addDefaultTable(company.getCompanyId, companyClassName)
    } match {
      case Failure(e: DuplicateTableNameException) =>
        logger.info(s"Table '$companyClassName' exists already")
        ExpandoTableLocalServiceUtil.getDefaultTable(company.getCompanyId, companyClassName)

      case Success(e) =>
        logger.info(s"Created '$companyClassName' table")
        e
    }

    Try {
      ExpandoColumnLocalServiceUtil.addColumn(table.getTableId, modeColumnName, ExpandoColumnConstants.STRING)
    } match {
      case Failure(e: DuplicateColumnNameException) =>
        logger.info(s"Column '$modeColumnName' exists already")

      case Success(e) =>
        logger.info(s"Created '$modeColumnName' column")
    }
  }

}
