package com.arcusys.learn.liferay.lrs.services

import com.liferay.portlet.expando.model.{ExpandoColumn, ExpandoColumnConstants, ExpandoTable, ExpandoValue}
import com.liferay.portlet.expando.service.{ExpandoColumnLocalServiceUtil, ExpandoTableLocalServiceUtil, ExpandoValueLocalServiceUtil}


object ExpandoLocalServiceHelper {
  val Strings = ExpandoColumnConstants.STRING

  def addColumn(tableId: Long, columnName: String, tpe: Int) =
    ExpandoColumnLocalServiceUtil.addColumn(tableId, columnName, tpe)

  def addDefaultTable(companyId: Long, companyClassName: String): ExpandoTable =
    ExpandoTableLocalServiceUtil.addDefaultTable(companyId, companyClassName)

  def addValue(companyId: Long,
               companyClassName: String,
               name: String,
               name1: String,
               classNameId: Long,
               mode: String): ExpandoValue =
    ExpandoValueLocalServiceUtil.addValue(companyId, companyClassName, name, name1, classNameId, mode)

  def deleteValue(companyId: Long,
                  companyClassName: String,
                  name: String,
                  name1: String,
                  classNameId: Long): Unit =
    ExpandoValueLocalServiceUtil.deleteValue(companyId, companyClassName, name, name1, classNameId)

  def getData(companyId: Long,
              companyClassName: String,
              name: String,
              name1: String,
              classNameId: Long,
              defaultData: String): String =
    ExpandoValueLocalServiceUtil .getData(companyId, companyClassName, name, name1, classNameId, defaultData)

  def getDefaultTable(companyId: Long, companyClassName: String): ExpandoTable = ExpandoTableLocalServiceUtil .getDefaultTable(companyId, companyClassName)

  def getColumn(tableId: Long, modeColumnName: String): ExpandoColumn = ExpandoColumnLocalServiceUtil.getColumn(tableId, modeColumnName)
}
