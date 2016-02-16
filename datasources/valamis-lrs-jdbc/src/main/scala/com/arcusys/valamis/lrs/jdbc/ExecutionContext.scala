package com.arcusys.valamis.lrs.jdbc

import scala.language.higherKinds
import scala.slick.driver.JdbcDriver
import scala.slick.lifted.{Query => Q, _}

/**
 * Created by Iliya Tryapitsin on 29.07.15.
 */
trait ExecutionContext {

  val driver: JdbcDriver

  import driver.simple._

  def run[T](c: => AppliedCompiledFunction[_, _, Seq[T]]): Seq[T]
  def run[T](c: => Rep[T]): T

  trait Invoker[U] {
    def value(v: U): Int
  }

  def insertTo [E, U, C[_]] (q: => Q[E,U,C]):  Invoker[U]
  def updateTo [E, U, C[_]] (q: => Q[E,U,C]):  Invoker[U]
  def delete   [E <: Table[_], U, C[_]] (q: => Q[E,U,C]):  Int

  def from [E, U] (q: => Q[E, U, Seq]): SelectInvoker[U]

  trait SelectInvoker[U] {

    def select: Seq[U]
    def selectFirst: U
    def selectFirstOpt: Option[U]

    def exists: Boolean
  }

}