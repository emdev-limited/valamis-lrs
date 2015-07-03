package com.arcusys.valamis.lrs.datasource

/**
 * Created by Iliya Tryapitsin on 04/01/15.
 */

/**
 * Base class for all entities that contains an id.
 */
trait WithOptionKey[T] {

  type KeyType = Option[T]

  type Type = T

  val key: KeyType

  def withId[M <: KeyType](e: M): WithOptionKey[T]
}

trait WithRequireKey[T] {

  type Type = T

  val key: T

  def withId[M <: Type](e: M): WithRequireKey[Type]
}