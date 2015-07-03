package com.arcusys.valamis.lrs.tincan

import scala.util.Try

/**
 * Created by Iliya Tryapitsin on 15/01/15.
 */
object AuthorizationScope extends Enumeration {
  type Type = Value

  val StatementsReadMine = Value(1, "statements/read/mine")
  val StatementsReadAll  = Value(2, "statements/read"     )
  val StateRead          = Value(3, "state/read"          )
  val Define             = Value(4, "define"              )
  val ProfileRead        = Value(5, "profile/read"        )
  val StateWrite         = Value(6, "state/write"         )
  val ProfileWrite       = Value(7, "profile/write"       )
  val StatementsWrite    = Value(8, "statements/write"    )

  val StatementsRead     = ValueSet(
    StatementsReadAll,
    StatementsReadMine
  )

  val AllRead            = ValueSet(
    StateRead,
    Define,
    ProfileRead
  ) ++ StatementsRead

  val All                = ValueSet(
    StateWrite,
    ProfileWrite,
    StatementsWrite
  ) ++ AllRead

  implicit class AuthorizationScopeExtension(val scope: Type) extends AnyVal {
    def toValueSet = ValueSet(scope)
  }

  implicit class AuthorizationScopeValueSetExtension(val scopes: ValueSet) extends AnyVal {
    def <==(other: ValueSet): Boolean = other.forall(scopes.contains)
    def contains(other: ValueSet) = other <== scopes

    def <=\=(other: ValueSet) = !other.forall(scopes.contains)
    def notContains(other: ValueSet) = other <=\= scopes

    def toStringParameter: String = scopes match {
      case All                  => "all"
      case AllRead              => "all/read"
      case StatementsRead       => "statements/read"
      case list if list.size == 1 => list.head.toString
      case _                    => throw new UnsupportedOperationException("unsupported value set")
    }
  }

  def fromString(str: String): ValueSet = str match {
    case null | ""         => ValueSet.apply()
    case "all"             => All
    case "all/read"        => AllRead
    case "statements/read" => StatementsRead
    case s                 => Try(withName(s).toValueSet).getOrElse(throw new UnsupportedOperationException("unsupported value set"))
  }
}
