package com.arcusys.valamis.lrs.jdbc

import javax.inject.Inject

import scala.language.higherKinds
import scala.reflect.ClassTag
import scala.slick.driver.JdbcDriver
import scala.slick.jdbc.JdbcBackend
import scala.slick.lifted.{Query => Q, _}

/**
 * Created by Iliya Tryapitsin on 29.07.15.
 */
class SimpleExecutionContext @Inject() (val driver: JdbcDriver,
                                        val db:     JdbcBackend#Database) extends ExecutionContext {
  import driver.simple._

  override def run[T](c: => AppliedCompiledFunction[_, _, Seq[T]]): Seq[T] =
    db withSession { implicit s => c run }

  override def run[T](c: => Rep[T]): T =
    db withSession { implicit s => c run }

  override def updateTo[E, U, C[_]](q: => Q[E, U, C]): Invoker[U] =
    new Invoker[U] {
      def value(v: U): Int = db withSession { implicit s => q update v }
    }

  override def insertTo[E, U, C[_]](q: => Q[E, U, C]): Invoker[U] =
    new Invoker[U] {
      def value(v: U): Int = db withSession { implicit s => q insert v }
    }

  override def delete[E <: Table[_], U, C[_]](q: => Q[E, U, C]): Int = db withSession { implicit s => q delete }

  def from [E, U] (q: => Q[E, U, Seq]): SelectInvoker[U] = new SelectInvokerImpl(q)

  class SelectInvokerImpl [U] (q: => Q[_, U, Seq]) extends SelectInvoker[U] {

    def selectFirst: U            = db withSession { implicit s => q first       }
    def selectFirstOpt: Option[U] = db withSession { implicit s => q firstOption }
    def select: Seq[U]            = db withSession { implicit s => q run         }
    def exists: Boolean           = db withSession { implicit s => q.firstOption.isDefined }

  }


}
