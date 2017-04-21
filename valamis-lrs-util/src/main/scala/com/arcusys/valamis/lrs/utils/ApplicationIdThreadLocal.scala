package com.arcusys.valamis.lrs.utils

object ApplicationIdThreadLocal  {
  private val _applicationId: ThreadLocal[String] = new ThreadLocal[String]

  def getApplicationId: String= {
    _applicationId.get()
  }

  def setApplicationId(application: String) {
    _applicationId.set(application)
  }
}
