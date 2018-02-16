package com.arcusys.valamis.lrs.liferay

import com.liferay.portal.kernel.events.SimpleAction

/**
  *
 * Created by Iliya Tryapitsin on 12.08.15.
 */
class LrsInitAction extends SimpleAction {

  override def run(strings: Array[String]): Unit = {
    LrsModeInitializer.init()
  }

}