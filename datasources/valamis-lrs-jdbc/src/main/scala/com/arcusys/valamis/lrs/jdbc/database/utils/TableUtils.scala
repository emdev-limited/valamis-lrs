package com.arcusys.valamis.lrs.jdbc.database.utils

import com.arcusys.valamis.lrs.utils._
import com.arcusys.valamis.lrs.jdbc.util.ast.AstHelpers

import scala.slick.driver.JdbcProfile
import scala.slick.lifted.Column

/**
 * Created by Iliya Tryapitsin on 30.07.15.
 */
trait TableUtils {

  this: AstHelpers =>

  implicit class TableExtensions[T <: JdbcProfile#Table[_]](val t: T) {
    def columns = {
      val cols =
        t.getClass.getMethods.toSeq
          .filter { f => f.getReturnType == classOf[Column[_]] && f.getParameterTypes.length == 0 }
          .map    { f => fieldSym(f.invoke(t).asInstanceOf[Column[_]].toNode) } ++
        t.getClass.getFields.toSeq
          .filter { f => f.getType == classOf[Column[_]] }
          .map    { f => fieldSym(f.asInstanceOf[Column[_]].toNode) }

      cols takeDefined
    }
  }
}
