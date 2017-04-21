package com.arcusys.valamis.lrs.guice

import java.sql.Connection
import javax.sql.DataSource

import com.arcusys.valamis.lrs.jdbc._
import com.arcusys.valamis.lrs.jdbc.database.SupportedDialect
import com.arcusys.valamis.lrs.jdbc.history.init.DbSchemaInit
import com.arcusys.valamis.lrs.jdbc.history.ver230.{DbSchemaUpgrade => Ver230}
import com.arcusys.valamis.lrs.jdbc.history.ver240.{DbSchemaUpgrade => Ver240}
import com.arcusys.valamis.lrs.jdbc.history.ver250.{DbSchemaUpgrade => Ver250}
import com.arcusys.valamis.lrs.jdbc.history.ver270.{DbSchemaUpgrade => Ver270}
import com.arcusys.valamis.lrs.jdbc.history.ver300.{DbSchemaUpgrade => Ver300}
import com.arcusys.valamis.lrs.jdbc.history.ver310.{DbSchemaUpgrade => Ver310}
import com.arcusys.valamis.lrs.{DbInit, DbUpgrade, EmptySparkProcessor, Lrs, SecurityManager, ValamisReporter}
import net.codingwell.scalaguice.ScalaModule

import scala.slick.driver.{JdbcDriver, JdbcProfile}
import scala.slick.jdbc.JdbcBackend

trait StorageModule extends ScalaModule {
  val ModuleType = "Jdbc"

  val dataSource: DataSource
  def dataAccessCleanup(connection: Connection)
  lazy val slickDriver: JdbcDriver = getSlickDriver
  lazy val databaseDef: slickDriver.profile.backend.DatabaseDef = slickDriver.profile.backend.Database.forDataSource(dataSource)

  private def getSlickDriver: JdbcDriver = {
    val connection = dataSource.getConnection
    try {
      val databaseMetaData = connection.getMetaData
      val dbName = databaseMetaData.getDatabaseProductName
      val dbMajorVersion = databaseMetaData.getDatabaseMajorVersion

      SupportedDialect.detectDialect(dbName, dbMajorVersion)
    } finally {
      dataAccessCleanup(connection)
    }
  }

  override def configure() {
    bind [JdbcDriver          ] toInstance slickDriver
    bind [JdbcProfile         ] toInstance slickDriver
    bind [JdbcBackend#Database] toInstance databaseDef

    bind [Lrs                    ].to[JdbcLrs                          ]
    bind [SecurityManager        ].to[JdbcSecurityManager              ]
    bind [ValamisReporter        ].to[JdbcValamisReporter              ]

    bind [DbInit].to[DbSchemaInit ]
    bind [DbUpgrade].annotatedWithName("ver230").to[Ver230 ]
    bind [DbUpgrade].annotatedWithName("ver240").to[Ver240 ]
    bind [DbUpgrade].annotatedWithName("ver250").to[Ver250 ]
    bind [DbUpgrade].annotatedWithName("ver270").to[Ver270 ]
    bind [DbUpgrade].annotatedWithName("ver300").to[Ver300 ]
    bind [DbUpgrade].annotatedWithName("ver310").to[Ver310 ]

  }
}




