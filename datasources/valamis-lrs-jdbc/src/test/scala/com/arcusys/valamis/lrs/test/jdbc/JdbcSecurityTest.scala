//package com.arcusys.valamis.lrs.test.jdbc
//
//import java.sql.Connection
//
//import com.arcusys.valamis.lrs.history.ver230.{DbSchemaUpgrade => Ver230}
//import com.arcusys.valamis.lrs.history.ver240.{DbSchemaUpgrade => Ver240}
//import com.arcusys.valamis.lrs.history.ver250.{DbSchemaUpgrade => Ver250}
//import com.arcusys.valamis.lrs.jdbc.{JdbcLrs, JdbcSecurityService}
//import com.arcusys.valamis.lrs.security.{AuthenticationStatus, AuthenticationType}
//import com.arcusys.valamis.lrs.tincan.AuthorizationScope
//import com.arcusys.valamis.lrs.{Lrs, SecurityServiceComponent}
//import org.scalatest.{BeforeAndAfter, FunSuite}
//
//import scala.slick.driver.H2Driver
//import scala.slick.driver.H2Driver.simple._
//
//
//class JdbcSecurityTest extends FunSuite with BeforeAndAfter {
//
//  val db = Database.forURL("jdbc:h2:mem:test3", driver = "org.h2.Driver")
//  var connection: Connection = _
//
//  var securityManager: SecurityServiceComponent = _
//  val driver = H2Driver
//  var lrs: Lrs = _
//
//  before {
//    connection = db.createConnection()
//
//    lrs = new JdbcLrs(driver, db, null)
//
//    val schema230 = new Ver230(driver, db, lrs.asInstanceOf[JdbcLrs])
//    schema230.upgrade
//
//    val schema240 = new Ver240(driver, db, lrs.asInstanceOf[JdbcLrs])
//    schema240.upgrade
//
//    val schema250 = new Ver250(driver, db)
//    schema250.upgrade
//
//    securityManager = new JdbcSecurityService(driver, db)
//
//  }
//  after {
//    val schema240 = new Ver240(driver, db, lrs.asInstanceOf[JdbcLrs])
//    schema240.downgrade
//    val schema230 = new Ver230(driver, db, lrs.asInstanceOf[JdbcLrs])
//    schema230.downgrade
////    sparkContext.stop()
//    connection.close()
//  }
//
//
//  test("register application and get applications") {
//    val newApp = securityManager.registrationApp("app1",Some("desc"),AuthorizationScope.All,AuthenticationType.OAuth)
//    val apps = securityManager.getApplications(1,0)
//    assert(1 == apps.size)
//    assert(apps.head.name == "app1")
//    assert(apps.headOption.equals(newApp))
//  }
//
//  test("get application") {
//    val newApp = securityManager.registrationApp("app1",Some("desc"),AuthorizationScope.All,AuthenticationType.OAuth)
//    val newApp2 = securityManager.registrationApp("app2",Some("desc2"),AuthorizationScope.AllRead,AuthenticationType.OAuth)
//    val resApp = securityManager.getApplication(newApp.get.appId)
//    val resApp2 = securityManager.getApplication(newApp2.get.appId)
//    assert(resApp.equals(newApp))
//    assert(resApp2.equals(newApp2))
//  }
//
//
//
//  test("block/unblock application") {
//    val newApp = securityManager.registrationApp("app1",Some("desc"),AuthorizationScope.All,AuthenticationType.OAuth)
//    val newApp2 = securityManager.registrationApp("app2",Some("desc2"),AuthorizationScope.AllRead,AuthenticationType.OAuth)
//    securityManager.blockApplication(newApp.get.appId)
//    val resApp = securityManager.getApplication(newApp.get.appId)
//    assert(!resApp.get.isActive)
//    securityManager.unblockApplication(newApp.get.appId)
//    val resApp2 = securityManager.getApplication(newApp.get.appId)
//    assert(resApp2.get.isActive)
//  }
//
//  test("update application") {
//    val newApp = securityManager.registrationApp("app1",Some("desc"),AuthorizationScope.All,AuthenticationType.OAuth)
//    val resApp = securityManager.getApplication(newApp.get.appId)
//    securityManager.updateApplication(newApp.get.appId, name = "newapp1", desc = Some("newdesc"), scope = AuthorizationScope.StatementsRead, AuthenticationType.Basic)
//    val resApp2 = securityManager.getApplication(newApp.get.appId)
//    assert(!resApp2.equals(resApp))
//    assert(resApp2.get.name.equals("newapp1"))
//  }
//
//  test("delete application") {
//    val newApp = securityManager.registrationApp("app1",Some("desc"),AuthorizationScope.All,AuthenticationType.OAuth)
//    val newApp2 = securityManager.registrationApp("app2",Some("desc2"),AuthorizationScope.AllRead,AuthenticationType.OAuth)
//    val resApp = securityManager.getApplication(newApp.get.appId)
//    securityManager.deleteApplication(newApp.get.appId)
//    val resApp2 = securityManager.getApplication(newApp.get.appId)
//    assert(resApp.isDefined)
//    assert(resApp2.isEmpty)
//    assert(securityManager.getApplications(100, 0).size == 1)
//  }
//
//  test("request token") {
//    val newApp = securityManager.registrationApp("app1",Some("desc"),AuthorizationScope.All,AuthenticationType.OAuth).get
//    securityManager.setRequestToken(newApp.appId, "rcode", "rsecret", "callback")
//    val resToken = securityManager.getRequestToken(newApp.appId, "rcode")
//    assert(resToken.isDefined)
//
//    val resToken2 = securityManager.getRequestToken(newApp.appId, "rcode1")
//    val callback = securityManager.getCallback(resToken.get.code)
//
//    assert(resToken2.isEmpty)
//    assert(callback.get.equals("callback"))
//  }
//
//  test("access token") {
//    val newApp = securityManager.registrationApp("app1",Some("desc"),AuthorizationScope.All,AuthenticationType.OAuth).get
//    securityManager.setRequestToken(newApp.appId, "rcode", "rsecret", "callback")
//    securityManager.setAccessToken(newApp.appId, "rcode", "acode", "asecret")
//    val resToken = securityManager.getAccessToken(newApp.appId, "acode")
//    val resToken2 = securityManager.getAccessToken(newApp.appId, "acode1")
//
//    assert(resToken.isDefined)
//    assert(resToken.get.token.get.equals("acode"))
//    assert(resToken2.isEmpty)
//  }
//
//  test("check by basic") {
//    val newApp = securityManager.registrationApp("app1",Some("desc"),AuthorizationScope.AllRead,AuthenticationType.Basic)
//    val newApp2 = securityManager.registrationApp("app2",Some("desc2"),AuthorizationScope.AllRead,AuthenticationType.Basic)
//    assert(securityManager.checkByBasic("app2", newApp.get.appSecret, AuthorizationScope.All)==AuthenticationStatus.Denied)
//    assert(securityManager.checkByBasic(newApp.get.appId, newApp.get.appSecret, AuthorizationScope.All)==AuthenticationStatus.Forbidden)
//    assert(securityManager.checkByBasic(newApp.get.appId, newApp.get.appSecret, AuthorizationScope.AllRead)==AuthenticationStatus.Allowed)
//  }
//
//  test("check by token") {
//    val newApp = securityManager.registrationApp("app1",Some("desc"),AuthorizationScope.AllRead,AuthenticationType.OAuth).get
//    val newApp2 = securityManager.registrationApp("app2",Some("desc2"),AuthorizationScope.AllRead,AuthenticationType.OAuth)
//    securityManager.setRequestToken(newApp.appId, "rcode", "rsecret", "callback")
//    val resToken = securityManager.getRequestToken(newApp.appId, "rcode")
//    assert(resToken.isDefined)
//
//    securityManager.setAccessToken(newApp.appId, resToken.get.code, "acode", "asecret")
//    val accessToken = securityManager.getAccessToken(newApp.appId, "acode")
//    assert(securityManager.checkByToken("app2",accessToken.get.token.orNull, AuthorizationScope.All)==AuthenticationStatus.Denied)
//    assert(securityManager.checkByToken(newApp.appId, "dfghdfg", AuthorizationScope.All)==AuthenticationStatus.Forbidden)
//    assert(securityManager.checkByToken(newApp.appId, "", AuthorizationScope.All)==AuthenticationStatus.Forbidden)
//    assert(securityManager.checkByToken(newApp.appId, null, AuthorizationScope.All)==AuthenticationStatus.Forbidden)
//    assert(securityManager.checkByToken(newApp.appId, accessToken.get.token.getOrElse(""), AuthorizationScope.AllRead)==AuthenticationStatus.Allowed)
//  }
//
//  test("clear tokens") {
//    val newApp = securityManager.registrationApp("app1",Some("desc"),AuthorizationScope.AllRead,AuthenticationType.OAuth).get
//    val newApp2 = securityManager.registrationApp("app2",Some("desc2"),AuthorizationScope.AllRead,AuthenticationType.OAuth).get
//    securityManager.setRequestToken(newApp.appId, "rcode", "rsecret", "callback")
//    val resToken = securityManager.getRequestToken(newApp.appId, "rcode")
//    assert(resToken.isDefined)
//    securityManager.setAccessToken(newApp.appId, resToken.get.code, "acode", "asecret")
//    val accessToken = securityManager.getAccessToken(newApp.appId, "acode").get
//
//    securityManager.setRequestToken(newApp2.appId, "rcode2", "rsecret2", "callback2")
//    val resToken2 = securityManager.getRequestToken(newApp2.appId, "rcode2")
//    assert(resToken2.isDefined)
//    securityManager.setAccessToken(newApp2.appId, resToken2.get.code, "acode2", "asecret2")
//    val accessToken2 = securityManager.getAccessToken(newApp2.appId, "acode2").get
//
//    securityManager.clear()
//    assert(securityManager.getAccessToken(newApp.appId, accessToken.token.get).isDefined)
//    assert(securityManager.getAccessToken(newApp2.appId, accessToken2.token.get).isDefined)
//  }
//}