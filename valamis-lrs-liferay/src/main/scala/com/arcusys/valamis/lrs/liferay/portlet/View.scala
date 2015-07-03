package com.arcusys.valamis.lrs.liferay.portlet

/**
 * Created by Iliya Tryapitsin on 27.04.15.
 */
object View extends Enumeration {
  type Type = Value

  val Delete = Value("delete")
  val Add   = Value("add")
  val Edit   = Value("edit")
  val List   = Value("list")

  val Name = "view"
}
