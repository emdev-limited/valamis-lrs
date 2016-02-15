package com.arcusys.valamis.lrs.liferay.util

import javax.inject.{Inject, Named}

import scala.concurrent.forkjoin.ForkJoinPool

class ForkJoinPoolWithDbScope @Inject()(@Named("Simple") dbContext: LiferayDbContext)
  extends ForkJoinPool {

  override def execute(task: Runnable) {
    super.execute(new Runnable {
      // this runs in current thread
      val scopeValue = dbContext.getScope

      override def run() = {
        // this runs in sub thread
        dbContext.setScope(scopeValue)

        task.run()
      }
    })
  }
}
