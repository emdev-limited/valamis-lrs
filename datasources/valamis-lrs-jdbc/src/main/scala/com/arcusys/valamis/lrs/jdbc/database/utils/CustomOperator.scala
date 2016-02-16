package com.arcusys.valamis.lrs.jdbc.database.utils

import com.arcusys.valamis.lrs._
import com.arcusys.valamis.lrs.jdbc.database.BaseDataContext
import com.arcusys.valamis.lrs.tincan.LanguageMap

import scala.slick.ast.{Library, LiteralNode}

/**
 * Created by Iliya Tryapitsin on 18.06.15.
 */
trait CustomOperator {
  this: BaseDataContext =>

  import executionContext.driver.simple._

  implicit class LanguageMapOptColumnExtension(l: Column[Option[LanguageMap]]) extends LanguageMapGenColumnExtension(l)

  implicit class LanguageMapColumnExtension   (l: Column[LanguageMap])         extends LanguageMapGenColumnExtension(l)

  class LanguageMapGenColumnExtension[T](l: Column[T]) {

    val likeQuery = SimpleExpression.binary[T, Option[String], Boolean] { (l, s, qb) =>
      s match {
        case s: LiteralNode =>

          val filterNode = s.value match {
            case Some(v) if v.isInstanceOf[String] =>
              LiteralNode(s"%$v%")

            case None =>
              LiteralNode(s"%")
          }

          val likeNode = Library.Like.typed[String](l, filterNode)
          qb.expr(likeNode)
      }
    }

    val notEmptyQuery = SimpleExpression.unary[T, Boolean] { (l, qb) =>
      val likeNode = Library.Like.typed[String](l, LiteralNode(s"[]"))
      val inversedLikeNode = Library.Not.typed[String](likeNode)
      qb.expr(inversedLikeNode)
    }

    def like(filter: Option[String]) = likeQuery(l, filter)

    def like(filter: ConstColumn[Option[String]]) = likeQuery(l, filter)

    def like(filter: String) = likeQuery(l, filter ?)

    def notEmpty = notEmptyQuery(l)
  }
}
