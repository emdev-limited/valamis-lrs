package com.arcusys.valamis.lrs.security

object ThreadedApplication {
  private val app = new ThreadLocal[Application]

  def getApplication: Option[Application] =
    Option(app.get())

  def setApplication(a: Option[Application]): Unit =
    a match {
      case Some(v) => app.set(v)
      case None => app.set(null)
    }
}
