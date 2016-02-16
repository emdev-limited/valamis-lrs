package com.arcusys.valamis.lrs.liferay.message

import com.arcusys.valamis.lrs.liferay.Loggable

/**
 * Created by Iliya Tryapitsin on 07.08.15.
 */
class SimpleBroker extends Broker with Loggable {
  override def !(msg: (String, String)): Unit = logger.info(s"Head: ${msg._1}\nContent: ${msg._2}")
}
