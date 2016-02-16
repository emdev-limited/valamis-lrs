package com.arcusys.valamis.lrs.liferay

import java.util.concurrent.ExecutorService

import com.arcusys.valamis.lrs.jdbc.ExecutionContext
import com.arcusys.valamis.lrs.jdbc._
import com.arcusys.valamis.lrs.jdbc.database._
import com.arcusys.valamis.lrs.jdbc.database.typemap.joda.{JodaSupport, SimpleJodaSupport}
import com.arcusys.valamis.lrs.liferay.message._
import com.arcusys.valamis.lrs.liferay.util.{ExecutorProvider, ForkJoinPoolWithDbScope, LiferayDbContext}
import com.arcusys.valamis.lrs.{Lrs, LrsType}
import com.liferay.portal.kernel.dao.jdbc.DataAccess
import com.liferay.portal.kernel.util.InfrastructureUtil
import net.codingwell.scalaguice.ScalaModule

import scala.concurrent.ExecutionContextExecutor
import scala.slick.driver.{JdbcDriver, JdbcProfile}
import scala.slick.jdbc.JdbcBackend

class LrsModule extends ScalaModule with LrsTypeLocator {

  lazy val dataSource = InfrastructureUtil.getDataSource
  lazy val slickDriver = getSlickDriver
  lazy val databaseDef = slickDriver.profile.backend.Database.forDataSource(dataSource)
  lazy val lrsType = LrsType.Simple

  private def getSlickDriver: JdbcDriver = {
    val connection = dataSource.getConnection
    try {
      val databaseMetaData = connection.getMetaData
      val dbName = databaseMetaData.getDatabaseProductName
      val dbMajorVersion = databaseMetaData.getDatabaseMajorVersion

      SupportedDialect.detectDialect(dbName, dbMajorVersion)
    } finally {
      DataAccess.cleanUp(connection)
    }
  }

  override def configure() {

    install(new ConfigModule)

    bind [JdbcDriver          ] toInstance slickDriver
    bind [JdbcProfile         ] toInstance slickDriver
    bind [JdbcBackend#Database] toInstance databaseDef

    bind [LiferayDbContext].annotatedWithName (LrsType.SimpleName).to[LiferayDbContext]

    bind [ExecutorService         ].annotatedWithName(LrsType.SimpleName).to[ForkJoinPoolWithDbScope ]
    bind [ExecutionContextExecutor].annotatedWithName(LrsType.SimpleName).toProvider[ExecutorProvider]

    bind [JodaSupport     ].annotatedWithName (LrsType.SimpleName).to[SimpleJodaSupport     ]
    bind [JdbcLrs         ].annotatedWithName (LrsType.SimpleName).to[SimpleLrs             ]
    bind [Lrs             ].annotatedWithName (LrsType.SimpleName).to[SimpleLrs             ]
    bind [SecurityManager ].annotatedWithName (LrsType.SimpleName).to[SimpleSecurityManager ]
    bind [ValamisReporter ].annotatedWithName (LrsType.SimpleName).to[SimpleValamisReporter ]
    bind [ExecutionContext].annotatedWithName (LrsType.SimpleName).to[SimpleExecutionContext]

    bind [JdbcLrs        ].annotatedWithName (LrsType.ExtendedName).to[SimpleLrs            ]
    bind [Lrs            ].annotatedWithName (LrsType.ExtendedName).to[SimpleLrs            ]
    bind [SecurityManager].annotatedWithName (LrsType.ExtendedName).to[SimpleSecurityManager]
    bind [ValamisReporter].annotatedWithName (LrsType.ExtendedName).to[SimpleValamisReporter]

    bind [Broker].to[KafkaBroker]
  }
}


