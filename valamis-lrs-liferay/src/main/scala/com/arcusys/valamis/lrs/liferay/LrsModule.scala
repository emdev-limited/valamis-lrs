package com.arcusys.valamis.lrs.liferay

import java.sql.Connection
import java.util.concurrent.ExecutorService
import javax.sql.DataSource

import com.arcusys.valamis.lrs.liferay.message._
import com.arcusys.valamis.lrs.liferay.util.{DbContext, ExecutorProvider, ForkJoinPoolWithDbScope, LiferayDbContext}
import com.arcusys.valamis.lrs.StorageModule
import com.liferay.portal.kernel.dao.jdbc.DataAccess
import com.liferay.portal.kernel.util.InfrastructureUtil

import scala.concurrent.ExecutionContextExecutor

object LrsModule extends StorageModule with Loggable {

  // for jdbc storage
  lazy val dataSource: DataSource = InfrastructureUtil.getDataSource
  def dataAccessCleanup(connection: Connection) = {
    DataAccess.cleanUp(connection)
  }

  override def configure() {

    install(new ConfigModule)

    bind [DbContext].to[LiferayDbContext]

    bind [ExecutorService         ].to[ForkJoinPoolWithDbScope ]
    bind [ExecutionContextExecutor].toProvider[ExecutorProvider]

    bind [Broker].to[KafkaBroker]

    super.configure()
  }
}


