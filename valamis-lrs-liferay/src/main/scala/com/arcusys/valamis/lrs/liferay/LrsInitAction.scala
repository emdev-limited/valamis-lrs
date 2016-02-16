package com.arcusys.valamis.lrs.liferay

import com.arcusys.valamis.lrs.RunningMode
import com.liferay.portal.kernel.events.SimpleAction

/**
 * Created by Iliya Tryapitsin on 12.08.15.
 */
class LrsInitAction extends SimpleAction with LrsTypeLocator with LrsModeLocator {
  override def run(strings: Array[String]): Unit = {
    initLrsTypeSettings()
    initLrsModeSettings()

    RunningMode setCurrent getLrsMode
  }
}