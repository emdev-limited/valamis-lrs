package com.arcusys.valamis.lrs.jdbc.database.row

/**
 * Created by Iliya Tryapitsin on 04/01/15.
 */

/**
 * Base class for all entities that contains an id.
 */
trait WithKey[T] extends Product{
  type KeyType
  type Type = T
  val key: KeyType
  def withId[M <: T](e: M): WithKey[T]
}

trait WithOptionKey[T] extends WithKey[T] {
  type KeyType = Option[T]
}

trait WithRequireKey[T] extends WithKey[T] {
  type KeyType = T
}