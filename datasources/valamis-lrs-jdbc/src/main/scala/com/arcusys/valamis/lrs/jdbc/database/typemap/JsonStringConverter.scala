package com.arcusys.valamis.lrs.jdbc.database.typemap

import com.arcusys.json.JsonHelper
import com.arcusys.valamis.lrs.jdbc.database.typemap.joda.converter.SqlTypeConverter
import com.arcusys.valamis.lrs.tincan._

/**
 * Created by Iliya Tryapitsin on 17.06.15.
 */
trait JsonStringConverter
  extends SqlTypeConverter[String, LanguageMap] {

  def toSqlType(z: LanguageMap): String =
    if (z == null || z.isEmpty) JsonHelper.toJson(Map[String, String]())
    else JsonHelper.toJson(z)

  def fromSqlType(z: String): LanguageMap =
    if (z == null) Map[String, String]()
    else JsonHelper.fromJson[LanguageMap](z)
}
