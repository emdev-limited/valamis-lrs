package com.arcusys.valamis.lrs.liferay.portlet

/**
 * Created by Iliya Tryapitsin on 27.04.15.
 */
object Action extends Enumeration {
  type Type = Value

  val Add    = Value("add")
  val Edit   = Value("edit")
  val Delete = Value("delete")
  val Block  = Value("block")
  val Unblock = Value("unblock")

  val Name = "action"
}
