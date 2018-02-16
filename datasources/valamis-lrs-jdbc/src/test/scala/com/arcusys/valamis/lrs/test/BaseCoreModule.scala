package com.arcusys.valamis.lrs.test

import com.arcusys.valamis.lrs.jdbc.history.BaseDbUpgrade
import com.arcusys.valamis.lrs.jdbc.history.ver230.{DbSchemaUpgrade => Ver230}
import com.arcusys.valamis.lrs.jdbc.history.ver240.{DbSchemaUpgrade => Ver240}
import com.arcusys.valamis.lrs.jdbc.history.ver250.{DbSchemaUpgrade => Ver250}
import com.arcusys.valamis.lrs.jdbc.history.ver270.{DbSchemaUpgrade => Ver270}
import com.arcusys.valamis.lrs.jdbc.history.ver300.{DbSchemaUpgrade => Ver300}
import com.arcusys.valamis.lrs.jdbc.history.ver310.{DbSchemaUpgrade => Ver310}
import com.arcusys.valamis.lrs.jdbc.{JdbcLrs, JdbcSecurityManager, JdbcValamisReporter}
import com.arcusys.valamis.lrs.test.config.DbInit
import com.arcusys.valamis.lrs._
import net.codingwell.scalaguice.ScalaModule
import org.apache.commons.logging.Log
import org.apache.commons.logging.impl.NoOpLog

import scala.concurrent.ExecutionContext.Implicits._
import scala.concurrent.ExecutionContextExecutor
import scala.slick.driver.{JdbcDriver, JdbcProfile}
import scala.slick.jdbc.JdbcBackend

/**
 * Created by Iliya Tryapitsin on 20/03/15.
 */
abstract class BaseCoreModule(val dbInit: DbInit) extends ScalaModule {
  override def configure(): Unit = {
    install(new ConfigModule)

    bind [JdbcDriver          ] toInstance dbInit.driver
    bind [JdbcProfile         ] toInstance dbInit.driver
    bind [JdbcBackend#Database] toInstance dbInit.conn
    bind [DbInit              ] toInstance dbInit

    bind [ExecutionContextExecutor].toInstance(global)
    bind[Log].toInstance(new NoOpLog)

    bind [Lrs             ].to[JdbcLrs                ]
    bind [SecurityManager ].to[JdbcSecurityManager    ]
    bind [ValamisReporter ].to[JdbcValamisReporter    ]

    bind [BaseDbUpgrade          ].annotatedWithName("ver230").to[Ver230 ]
    bind [BaseDbUpgrade          ].annotatedWithName("ver240").to[Ver240 ]
    bind [BaseDbUpgrade          ].annotatedWithName("ver250").to[Ver250 ]
    bind [BaseDbUpgrade          ].annotatedWithName("ver270").to[Ver270 ]
    bind [BaseDbUpgrade          ].annotatedWithName("ver300").to[Ver300 ]
    bind [BaseDbUpgrade          ].annotatedWithName("ver310").to[Ver310 ]
  }
}
