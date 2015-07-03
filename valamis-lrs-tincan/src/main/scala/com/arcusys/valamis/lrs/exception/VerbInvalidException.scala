package com.arcusys.valamis.lrs.exception

/**
 * Created by Iliya Tryapitsin on 11/02/15.
 */
class VerbInvalidException(message: String, cause: Throwable) extends Exception(message, cause) {
  def this(message: String) = this(message, null)
}
