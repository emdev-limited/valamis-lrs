package com.arcusys.valamis.lrs.liferay.exception

/**
 * Created by Iliya Tryapitsin on 24/01/15.
 */
class DocumentInvalidContentTypeException(message: String, cause: Throwable) extends Exception(message, cause) {
  def this(message: String) = this(message, null)
}
