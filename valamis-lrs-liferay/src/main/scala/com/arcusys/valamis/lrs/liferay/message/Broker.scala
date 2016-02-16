package com.arcusys.valamis.lrs.liferay.message

/**
 * Created by Iliya Tryapitsin on 07.08.15.
 */
trait Broker {
  def ! (msg: (String, String))
}
