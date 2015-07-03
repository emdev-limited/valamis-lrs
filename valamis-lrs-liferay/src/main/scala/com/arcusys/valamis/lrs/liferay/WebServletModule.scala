package com.arcusys.valamis.lrs.liferay

import com.arcusys.valamis.lrs.auth.AuthModule
import com.arcusys.valamis.lrs.datasource.SupportedDialect
import com.arcusys.valamis.lrs.liferay.filter._
import com.arcusys.valamis.lrs.liferay.servlet._
import com.arcusys.valamis.lrs.liferay.servlet.oauth._
import com.arcusys.valamis.lrs.liferay.servlet.valamis.VerbServlet
import com.google.inject.servlet.ServletModule
import com.liferay.portal.kernel.dao.jdbc.DataAccess
import com.liferay.portal.kernel.util.InfrastructureUtil
import net.codingwell.scalaguice.ScalaModule

import scala.slick.driver.{JdbcDriver, JdbcProfile}
import scala.slick.jdbc.JdbcBackend

/**
 * Created by Iliya Tryapitsin on 10/01/15.
 */
class WebServletModule extends ServletModule with ScalaModule {

  lazy val dataSource = InfrastructureUtil.getDataSource
  lazy val slickDriver = getSlickDriver
  lazy val databaseDef = slickDriver.profile.backend.Database.forDataSource(dataSource)

  def lrsUrlPrefix = "/xapi"

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

  override def configureServlets() {

    install(new ConfigModule)
//    install(new AkkaModule)

    bind[JdbcDriver]            .toInstance(slickDriver)
    bind[JdbcProfile]           .toInstance(slickDriver)
    bind[JdbcBackend#Database]  .toInstance(databaseDef)

    val all = filter(
      s"$lrsUrlPrefix/activities/profile*",
      s"$lrsUrlPrefix/activities/state*",
      s"$lrsUrlPrefix/activity/state*",
      s"$lrsUrlPrefix/agents/profile*",
      s"$lrsUrlPrefix/statements*",
      s"$lrsUrlPrefix/activities",
      s"$lrsUrlPrefix/agents",
      s"$lrsUrlPrefix/verb")

    all.through(classOf[MethodOverrideFilter])
    all.through(classOf[AuthenticationFilter])
    all.through(classOf[XApiVersionFilter])

    serve(s"$lrsUrlPrefix/verb*"              ).`with`(classOf[VerbServlet           ])
    serve(s"$lrsUrlPrefix/about*"             ).`with`(classOf[AboutServlet          ])
    serve(s"$lrsUrlPrefix/agents"             ).`with`(classOf[AgentServlet          ])
    serve(s"$lrsUrlPrefix/activities"         ).`with`(classOf[ActivityServlet       ])
    serve(s"$lrsUrlPrefix/statements"         ).`with`(classOf[StatementServlet      ])
    serve(s"$lrsUrlPrefix/OAuth/token"        ).`with`(classOf[TokenServlet          ])
    serve(s"$lrsUrlPrefix/activity/state*"    ).`with`(classOf[StateProfileServlet   ])
    serve(s"$lrsUrlPrefix/agents/profile*"    ).`with`(classOf[AgentProfileServlet   ])
    serve(s"$lrsUrlPrefix/OAuth/initiate"     ).`with`(classOf[RequestTokenServlet   ])
    serve(s"$lrsUrlPrefix/OAuth/authorize"    ).`with`(classOf[AuthorizeServlet      ])
    serve(s"$lrsUrlPrefix/activities/state*"  ).`with`(classOf[StateProfileServlet   ])
    serve(s"$lrsUrlPrefix/activities/profile*").`with`(classOf[ActivityProfileServlet])

    install(new AuthModule)

    super.configureServlets()
  }
}

