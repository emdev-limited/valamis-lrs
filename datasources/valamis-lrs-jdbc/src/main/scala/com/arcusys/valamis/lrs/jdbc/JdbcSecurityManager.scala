package com.arcusys.valamis.lrs.jdbc

import javax.inject.{Inject, Named}

import com.arcusys.valamis.lrs.SecurityManagerImpl
import com.arcusys.valamis.lrs.jdbc.database.typemap.joda.JodaSupport

import scala.slick.driver.JdbcDriver
import scala.slick.jdbc.JdbcBackend


class JdbcSecurityManager  @Inject() (val driver: JdbcDriver,
                                        val db:     JdbcBackend#Database)
  extends SecurityManagerImpl with JdbcSecurityComponent