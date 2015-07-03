package com.arcusys.valamis.lrs.datasource.row

/**
 * Created by iliyatryapitsin on 10.04.15.
 */
object ContextActivityType extends Enumeration {

  import com.arcusys.valamis.lrs.datasource.DbNameUtils

  type Type = Value

  val category = Value(DbNameUtils.category)
  val grouping = Value(DbNameUtils.grouping)
  val other    = Value(DbNameUtils.other   )
  val parent   = Value(DbNameUtils.parent  )
}
