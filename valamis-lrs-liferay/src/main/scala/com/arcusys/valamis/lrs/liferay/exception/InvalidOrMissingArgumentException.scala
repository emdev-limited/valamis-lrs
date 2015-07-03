package com.arcusys.valamis.lrs.liferay.exception

/**
 * Created by Iliya Tryapitsin on 15/01/15.
 */
case class InvalidOrMissingArgumentException(argument: String) extends Exception {
  override def getMessage = s"Argument $argument is missing or invalid"
}
