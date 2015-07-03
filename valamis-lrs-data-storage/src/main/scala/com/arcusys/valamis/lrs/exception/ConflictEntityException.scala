package com.arcusys.valamis.lrs.exception

import scala.slick.SlickException

/**
 * Created by Iliya Tryapitsin on 10/02/15.
 */
class ConflictEntityException(msg: String) extends SlickException(msg)