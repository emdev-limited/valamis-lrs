package com.arcusys.valamis.lrs.liferay

import com.arcusys.valamis.lrs._
import com.liferay.portal.kernel.util.{PropsKeys, PropsUtil}
import com.liferay.portal.model.Company
import com.liferay.portal.service.{CompanyLocalServiceUtil, ClassNameLocalServiceUtil}
import com.liferay.portal.util.PortalUtil
import com.liferay.portlet.expando.{DuplicateColumnNameException, DuplicateTableNameException}
import com.liferay.portlet.expando.model.ExpandoColumnConstants
import com.liferay.portlet.expando.service._

import scala.util._

/**
 * Created by Iliya Tryapitsin on 10.08.15.
 */
trait LrsTypeLocator extends Loggable {
  lazy private val defaultCompanyId    = PortalUtil.getDefaultCompanyId
  lazy private val company             = CompanyLocalServiceUtil.getCompany(defaultCompanyId)
  lazy private val companyClassName    = classOf[Company].getName
  lazy private val classNameId         = ClassNameLocalServiceUtil.getClassNameId(companyClassName)
  lazy private val lrsTypeColumnName   = "VALAMIS_LRS_TYPE"
  lazy private val msgBusColumnName    = "VALAMIS_LRS_MSG_BUS"

  def getLrsType: LrsType.Type = {

    val table  = ExpandoTableLocalServiceUtil .getDefaultTable(company.getCompanyId, companyClassName)
    val column = ExpandoColumnLocalServiceUtil.getColumn      (table.getTableId, lrsTypeColumnName)
    val value  = ExpandoValueLocalServiceUtil .getData(
      company.getCompanyId,
      companyClassName,
      table.getName,
      column.getName,
      classNameId,
      LrsType.Simple.toString
    )

    LrsType.withName(value toString)
  }

  def getMsgBusAddress: String = {

    val table  = ExpandoTableLocalServiceUtil .getDefaultTable(company.getCompanyId, companyClassName)
    val column = ExpandoColumnLocalServiceUtil.getColumn      (table.getTableId,     msgBusColumnName)
    ExpandoValueLocalServiceUtil .getData(
      company.getCompanyId,
      companyClassName,
      table.getName,
      column.getName,
      classNameId,
      EmptyString
    )
  }

  def saveLrsSettings(tpe: String, msgBus: String) = {

    val table  = ExpandoTableLocalServiceUtil.getDefaultTable(company.getCompanyId, companyClassName)
    val lrsTypeCol = ExpandoColumnLocalServiceUtil.getColumn(table.getTableId, lrsTypeColumnName)
    val msgBusCol  = ExpandoColumnLocalServiceUtil.getColumn(table.getTableId, msgBusColumnName)

    ExpandoValueLocalServiceUtil.deleteValue(company.getCompanyId, companyClassName, table.getName, lrsTypeCol.getName, classNameId)
    ExpandoValueLocalServiceUtil.addValue   (company.getCompanyId, companyClassName, table.getName, lrsTypeCol.getName, classNameId, tpe)

    if(msgBus != null && msgBus.isDefined) {
      ExpandoValueLocalServiceUtil.deleteValue(company.getCompanyId, companyClassName, table.getName, msgBusCol.getName, classNameId)
      ExpandoValueLocalServiceUtil.addValue   (company.getCompanyId, companyClassName, table.getName, msgBusCol.getName, classNameId, msgBus)
    }
  }

  def initLrsTypeSettings(): Unit = {

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
      ExpandoColumnLocalServiceUtil.addColumn(table.getTableId, lrsTypeColumnName, ExpandoColumnConstants.STRING)
    } match {
      case Failure(e: DuplicateColumnNameException) =>
        logger.info(s"Column '$lrsTypeColumnName' exists already")

      case Success(e) =>
        logger.info(s"Created '$lrsTypeColumnName' column")
    }

    Try {
      ExpandoColumnLocalServiceUtil.addColumn(table.getTableId, msgBusColumnName, ExpandoColumnConstants.STRING)
    } match {
      case Failure(e: DuplicateColumnNameException) =>
        logger.info(s"Column '$msgBusColumnName' exists already")

      case Success(e) =>
        logger.info(s"Created '$msgBusColumnName' column")
    }
  }

}
