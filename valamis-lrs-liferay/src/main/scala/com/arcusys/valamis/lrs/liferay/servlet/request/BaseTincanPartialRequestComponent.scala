package com.arcusys.valamis.lrs.liferay.servlet.request

/**
 * Created by Iliya Tryapitsin on 18.06.15.
 */
trait BaseTincanPartialRequestComponent {
  r: BaseLrsRequest =>

  def limit   = optionalInt(Limit)       getOrElse 100
  def offset  = optionalInt(Offset)      getOrElse 0
  def ascSort = optionalBoolean(AscSort) getOrElse true

  val Limit   = "limit"
  val Offset  = "offset"
  val AscSort = "asc-sort"
}
