package com.arcusys.valamis.lrs.liferay

import com.arcusys.valamis.lrs.utils.RunningMode

/**
  * Created by pkornilov on 11.08.16.
  */
object LrsModeInitializer extends LrsModeLocator {

  def init(): Unit = {
    logger.info("Init LRS mode...")
    try {
      initLrsModeSettings()
      RunningMode setCurrent getLrsMode
    } catch {
      case ex: Exception =>
        RunningMode.setCurrent(RunningMode.Development)
        logger.warn("Failed to init Lrs Mode. Using default mode: " + RunningMode.Development, ex)
    }
  }

}
