package com.arcusys.valamis.lrs.jdbc

import org.slf4j.LoggerFactory

/**
 * Created by Iliya Tryapitsin on 10.07.15.
 */
trait Loggable {
  val logger = LoggerFactory.getLogger("com.arcusys")
}
