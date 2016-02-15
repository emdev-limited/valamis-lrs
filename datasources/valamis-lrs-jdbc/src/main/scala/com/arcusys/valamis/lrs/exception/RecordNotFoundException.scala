package com.arcusys.valamis.lrs.exception

import scala.slick.SlickException

/**
 * Created by Iliya Tryapitsin on 22/01/15.
 */
class RecordNotFoundException(msg: String) extends SlickException(msg)
