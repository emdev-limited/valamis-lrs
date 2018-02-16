package com.arcusys.valamis.lrs.liferay

import java.sql.Connection
import java.util.concurrent.ExecutorService
import javax.sql.DataSource

import com.arcusys.learn.liferay.lrs.util.LiferayLogSupport
import com.arcusys.valamis.lrs.guice.StorageModule
import com.arcusys.valamis.lrs.liferay.message._
import com.arcusys.valamis.lrs.liferay.util._
import com.liferay.portal.kernel.dao.jdbc.DataAccess
import com.liferay.portal.kernel.util.{InfrastructureUtil, PropsUtil}
import org.apache.commons.logging.Log

import scala.concurrent.ExecutionContextExecutor
import scala.concurrent.forkjoin.ForkJoinPool
import scala.util.Try

object LrsModule extends StorageModule with Loggable with LiferayLogSupport {

  // for jdbc storage
  lazy val dataSource: DataSource = InfrastructureUtil.getDataSource
  def dataAccessCleanup(connection: Connection) = {
    DataAccess.cleanUp(connection)
  }

  override def configure() {

    install(new ConfigModule)

    bind[Log].toInstance(log)

    if (ModuleType == "Jdbc") {
      bind[DbContext].to[LiferayDbContext]
      bind[ExecutorService].to[ForkJoinPoolWithDbScope]
      bind[ExecutionContextExecutor].toProvider[ExecutorProvider]
    } else {
      bind[DbContext].to[DummyDbContext]
      bind[ExecutorService].to[ForkJoinPool]
      bind[ExecutionContextExecutor].toProvider[ExecutorProvider]
    }

    bind[Broker].to[KafkaBroker]

    super.configure()
  }
}

