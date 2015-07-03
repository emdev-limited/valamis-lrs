package com.arcusys.valamis.lrs.tincan

/**
  * Created by iliyatryapitsin on 08.04.15.
 */
object LanguageMapValidator {

  private val LANG_EXCLUDE_LIST = Seq("und")

  def checkRequirements(v:(String, String)): (String, String) = {
    import org.apache.commons.lang.LocaleUtils
    if(!LANG_EXCLUDE_LIST.contains(v._1))
      LocaleUtils.toLocale(v._1.replace("-", "_"))

    v
  }

  def checkRequirements(v: LanguageMap): LanguageMap = v map { lang =>
    LanguageMapValidator checkRequirements lang
  }

  def checkRequirements(v: Option[LanguageMap]): Option[LanguageMap] = v match {
    case Some(map) => Some(LanguageMapValidator checkRequirements map)
    case None      => None
  }
}
