package com.arcusys.valamis.lrs.jdbc.database.typemap

import com.arcusys.valamis.lrs.jdbc.database._
import com.arcusys.valamis.lrs.jdbc.database.typemap.tincan.{StatementObjectTypeSupport, LanguageMapSupport}

/**
 * Created by Iliya Tryapitsin on 17.06.15.
 */
trait CustomTypeExtension {
  this: BaseDataContext =>

  protected implicit val languageMapSupport = new LanguageMapSupport(driver)
  protected implicit val objTypeSupport     = new StatementObjectTypeSupport(driver)

  implicit val languageMapTypeMapper         = languageMapSupport.typeMapper
  implicit val getLanguageMapResult          = languageMapSupport.getResult.getResult
  implicit val getLanguageMapOptionResult    = languageMapSupport.getResult.getOptionResult
  implicit val setLanguageMapParameter       = languageMapSupport.setParameter.setParameter
  implicit val setLanguageMapOptionParameter = languageMapSupport.setParameter.setOptionParameter

  implicit val objTypeMapper             = objTypeSupport.typeMapper
  implicit val getObjTypeResult          = objTypeSupport.getResult.getResult
  implicit val getObjTypeOptionResult    = objTypeSupport.getResult.getOptionResult
  implicit val setObjTypeParameter       = objTypeSupport.setParameter.setParameter
  implicit val setObjTypeOptionParameter = objTypeSupport.setParameter.setOptionParameter
}
