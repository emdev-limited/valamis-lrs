package com.arcusys.valamis.lrs

import java.net.URI
import java.util.UUID

/**
 * Created by Iliya Tryapitsin on 25.04.15.
 */
package object utils {

  val EmptyString = ""

  implicit class AnyExtension[T <: Any](val obj: T) extends AnyVal {
    def toOption: Option[T] = obj ?

    def ? : Option[T] = Option(obj)

    def afterThat[E] (act: (T) => E) = act(obj)

    def toSeq = Seq(obj)

    def toPartialSeq (isFull: Boolean = true): PartialSeq[T] = obj.toSeq.toPartialSeq (isFull)
  }

  implicit class SeqExtension [T] (val seq: Seq[T]) extends AnyVal {
    def toPartialSeq (isFull: Boolean): PartialSeq[T] = PartialSeq (seq, isFull)
  }

  implicit class SeqOptionExtension [T] (val seq: Seq[Option[T]]) extends AnyVal {
    def takeDefined(): Seq[T] = seq filter { _ isDefined } map { r => r get }
  }

  implicit class OptionExtension[T] (val obj: Option[T]) extends AnyVal {
    def whenDefined [E] (act: (T) => E) = obj map { x => act(x) }

    def toPartialSeq (isFull: Boolean): PartialSeq[T]  = obj.toIndexedSeq.toPartialSeq (isFull)

    def toPartialSeq: PartialSeq[T]  = toPartialSeq(true)
  }

  implicit class BooleanExtension (val boolean: Boolean) extends AnyVal {
    def whenTrue (act: (Boolean) => Unit) = {
      if (boolean) act(boolean)

      boolean
    }

    def whenFalse (act: (Boolean) => Unit) = {
      if (!boolean) act(boolean)

      boolean
    }
  }

  implicit class StringExtension (val str: String) extends AnyVal {
    def isDefined = !str.isEmpty

    def removeAll(pattern: String) = str.replaceAll(pattern, EmptyString)

    def toURI = new URI(str)

    def toUUID = UUID.fromString (str)
  }
}