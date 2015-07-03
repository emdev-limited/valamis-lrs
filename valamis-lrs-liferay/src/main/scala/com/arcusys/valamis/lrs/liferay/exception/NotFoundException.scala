package com.arcusys.valamis.lrs.liferay.exception

/**
 * Created by Iliya Tryapitsin on 15/01/15.
 */
case class NotFoundException(resType: String) extends Exception {
  override def getMessage = s"Requested resource $resType was not found"
}
