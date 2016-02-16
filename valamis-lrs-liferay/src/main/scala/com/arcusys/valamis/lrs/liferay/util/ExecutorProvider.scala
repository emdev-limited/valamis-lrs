package com.arcusys.valamis.lrs.liferay.util

import java.util.concurrent.ExecutorService
import javax.inject.Named
import com.google.inject.{Inject, Provider}
import scala.concurrent.ExecutionContextExecutor

class ExecutorProvider @Inject()(@Named("Simple") executorService: ExecutorService)
  extends Provider[ExecutionContextExecutor] {

  override def get() = {
    concurrent.ExecutionContext.fromExecutorService(executorService)
  }
}
