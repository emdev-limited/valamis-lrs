package com.arcusys.valamis.lrs.datasource

/**
 * Created by Iliya Tryapitsin on 17.06.15.
 */
trait CustomTypeExtension {
  this: DataContext =>

  protected implicit val languageMapSupport = new LanguageMapSupport(driver)
  protected implicit val objTypeSupport     = new StatementObjectTypeSupport(driver)

  implicit val languageMapTypeMapper         = languageMapSupport.TypeMapper
  implicit val getLanguageMapResult          = languageMapSupport.LanguageMapGetResult.getResult
  implicit val getLanguageMapOptionResult    = languageMapSupport.LanguageMapGetResult.getOptionResult
  implicit val setLanguageMapParameter       = languageMapSupport.LanguageMapSetParameter.setJodaParameter
  implicit val setLanguageMapOptionParameter = languageMapSupport.LanguageMapSetParameter.setJodaOptionParameter

  implicit val objTypeMapper             = objTypeSupport.TypeMapper
  implicit val getObjTypeResult          = objTypeSupport.StatementObjectTypeGetResult.getResult
  implicit val getObjTypeOptionResult    = objTypeSupport.StatementObjectTypeGetResult.getOptionResult
  implicit val setObjTypeParameter       = objTypeSupport.StatementObjectTypeSetParameter.setJodaParameter
  implicit val setObjTypeOptionParameter = objTypeSupport.StatementObjectTypeSetParameter.setJodaOptionParameter


}
