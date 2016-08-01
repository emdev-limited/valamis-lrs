package com.arcusys.valamis.lrs.liferay.util

import javax.inject.Inject

import scala.concurrent.forkjoin.ForkJoinPool

class ForkJoinPoolWithDbScope @Inject()(dbContext: DbContext)
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
