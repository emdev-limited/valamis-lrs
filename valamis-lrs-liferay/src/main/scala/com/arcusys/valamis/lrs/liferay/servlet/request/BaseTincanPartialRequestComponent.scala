package com.arcusys.valamis.lrs.liferay.servlet.request

/**
 * Created by Iliya Tryapitsin on 18.06.15.
 */
trait BaseTincanPartialRequestComponent {
  r: BaseLrsRequest =>

  // Handle limit with 0
  def limit   = optionalInt(Limit) getOrElse 100 match { case x if (x <= 0) => 100 case value => value }
  def offset  = optionalInt(Offset) getOrElse 0
  def nameSort = optionalBoolean(NameSort) getOrElse true
  def timeSort = optionalBoolean(TimeSort) getOrElse false
  def sortTimeFirst = optionalBoolean(SortTimeFirst) getOrElse false

  val TimeSort        = "time-sort"
  val SortTimeFirst   = "sortTimeFirst"
  val Limit           = "limit"
  val Offset          = "offset"
  val NameSort        = "name-sort"
}
