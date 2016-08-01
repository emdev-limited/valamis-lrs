package com.arcusys.valamis.lrs.jdbc.database.utils

import com.arcusys.valamis.lrs.jdbc.database.BaseDataContext
import com.arcusys.valamis.lrs.tincan.LanguageMap

import scala.slick.ast.{Library, LiteralNode, Node}
import scala.slick.lifted.ExtensionMethods
import scala.slick.lifted.FunctionSymbolExtensionMethods.functionSymbolExtensionMethods

/**
  * Created by Iliya Tryapitsin on 18.06.15.
  */
trait CustomOperator {
  this: BaseDataContext =>

  import driver.simple._

  implicit class LanguageMapOptColumnExtension(l: Column[Option[LanguageMap]]) extends LanguageMapGenColumnExtension(l)

  implicit class LanguageMapColumnExtension(l: Column[LanguageMap]) extends LanguageMapGenColumnExtension(l)

  class LanguageMapGenColumnExtension[T](val c: Column[T]) extends ExtensionMethods[LanguageMap, T] {

    private def convertLiteralNode(e: Node) = e match {
      case s: LiteralNode =>
        s.value match {
          case Some(v) if v.isInstanceOf[String] =>
            LiteralNode(s"%$v%")
          case None =>
            LiteralNode(s"%")
          case v: String => LiteralNode(s"%$v%")
        }
      case _ => e
    }

    def notEmpty[R](implicit om: o#to[Boolean, R]) =
      Library.Not.column[Boolean](om.column(Library.Like, n, LiteralNode("{}")).toNode)

    def like[P2, R](e: Column[P2], esc: Char = '\u0000')(implicit om: o#arg[String, P2]#to[Boolean, R]) =
      if (esc == '\u0000') om.column(Library.Like, n, convertLiteralNode(e.toNode))
      else om.column(Library.Like, n, convertLiteralNode(e.toNode), LiteralNode(esc))

  }

}
