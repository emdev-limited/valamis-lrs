package com.arcusys.valamis.lrs.datasource

import com.arcusys.valamis.lrs.tincan.LanguageMap

import scala.slick.ast.{Library, LiteralNode}

/**
 * Created by Iliya Tryapitsin on 18.06.15.
 */
trait CustomQuery {
  this: DataContext =>

  import driver.simple._

  implicit class LanguageMapColumnExtension(l: Column[LanguageMap]) {

    val likeQuery = SimpleExpression.binary[LanguageMap, Option[String], Boolean] { (l, s, qb) =>
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

    def like(filter: Option[String]) = likeQuery(l, filter)
  }
}
