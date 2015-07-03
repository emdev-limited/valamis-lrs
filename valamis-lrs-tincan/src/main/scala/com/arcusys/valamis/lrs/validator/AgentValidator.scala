package com.arcusys.valamis.lrs.validator

import org.json4s.JsonAST.JValue

/**
 * Created by Iliya Tryapitsin on 24/03/15.
 */
object AgentValidator {
  def check(jValue: JValue) = ActorValidator checkNotNull jValue
}
