package com.arcusys.valamis

/**
 * Created by Iliya Tryapitsin on 25.04.15.
 */
package object lrs {
  implicit class AnyExtension[T <: Any](val obj: T) extends AnyVal {
    def toOption: Option[T] = Option(obj)
  }
}