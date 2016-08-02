package com.arcusys.valamis.lrs.liferay

import com.arcusys.valamis.lrs.{LrsType, RunningMode}
import com.liferay.portal.kernel.events.SimpleAction

/**
 * Created by Iliya Tryapitsin on 12.08.15.
 */
class LrsInitAction extends SimpleAction with LrsModeLocator {
  override def run(strings: Array[String]): Unit = {
    initLrsModeSettings()

    RunningMode setCurrent getLrsMode
  }
}