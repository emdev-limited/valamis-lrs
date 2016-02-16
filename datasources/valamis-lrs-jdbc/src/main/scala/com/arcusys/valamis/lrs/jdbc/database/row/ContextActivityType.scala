package com.arcusys.valamis.lrs.jdbc.database.row

/**
 * Created by iliyatryapitsin on 10.04.15.
 */
object ContextActivityType extends Enumeration {

  import com.arcusys.valamis.lrs.jdbc.database.utils.DbNameUtils

  type Type = Value

  val Category = Value(DbNameUtils.category)
  val Grouping = Value(DbNameUtils.grouping)
  val Other    = Value(DbNameUtils.other   )
  val Parent   = Value(DbNameUtils.parent  )
}
