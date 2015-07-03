package com.arcusys.valamis.lrs.tincan.test

import com.arcusys.valamis.lrs.tincan.AuthorizationScope
import org.scalatest.FlatSpec

/**
 * Created by Iliya Tryapitsin on 12.05.15.
 */
class AuthorizationScopeTests extends FlatSpec {
  "AuthorizationScope" should "return value set as a long number" in {
    val tits = AuthorizationScope
      .ValueSet(
        AuthorizationScope.StatementsReadAll,
        AuthorizationScope.StatementsReadMine)
      .toBitMask
      .sum

    val scope = AuthorizationScope.ValueSet.fromBitMask(Array(tits))
    assert(scope.contains(AuthorizationScope.StatementsReadAll))
    assert(scope.contains(AuthorizationScope.StatementsReadMine))
    assert(scope == AuthorizationScope.StatementsRead)
    assert(scope <== AuthorizationScope.ValueSet())
    assert(AuthorizationScope.All <== scope)
    assert(!scope.contains(AuthorizationScope.StatementsWrite))
  }
}
