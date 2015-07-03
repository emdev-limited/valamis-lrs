package com.arcusys.valamis.lrs.liferay.portlet

import com.arcusys.valamis.lrs.liferay.WaitPeriod._
import com.arcusys.valamis.lrs.tincan.AuthorizationScope

import scala.concurrent.ExecutionContext.Implicits.global
import javax.portlet._
import akka.util.Timeout
import html.apps.html._
import scala.util._
import scala.concurrent._
import scala.concurrent.duration._
import akka.pattern.Patterns
import akka.actor.ActorSystem
import com.arcusys.valamis.lrs.GuiceAkkaExtension
import com.arcusys.valamis.lrs.auth._
import com.arcusys.valamis.lrs._
import com.arcusys.valamis.lrs.liferay.{LrsUtils, WebServletModule}
import com.google.inject._

/**
 * Created by Iliya Tryapitsin on 21.04.15.
 */
class LrsAppsView extends GenericPortlet {

  val injector = Guice.createInjector(new WebServletModule)
  val authentication = injector.getInstance(classOf[Authentication])
//  val system = injector.getInstance(classOf[ActorSystem])
//  val authenticationActor = system.actorOf(GuiceAkkaExtension(system).props(AuthenticationActor.name))
//  val duration = 10 seconds
//  implicit val timeout = Timeout(duration)

  private def getOffset(r: PortletRequest) = {
    val o = r.getParameter("offset")
    if(o == null || o.isEmpty) 0.toString
    else o
  }

  private def getAppsCount(r: PortletRequest) = {
    val a = r.getParameter("appsCount")
    if(a == null || a.isEmpty) 10.toString
    else a
  }

  private def getShowView(r: PortletRequest) = {
    val s = r.getParameter(View.Name)
    if(s == null || s.isEmpty) View.List
    else View.withName(s)
  }

  private def getAction(r: PortletRequest) = {
    val a = r.getParameter(Action.Name)
    if(a == null || a.isEmpty) None
    else Action.withName(a).toOption
  }

  private def getSelectedApp(r: PortletRequest) = {
    val a = Some(r.getParameter("appId"))
    a match {
      case Some(id) => authentication.GetApplication(id)

      case _ => None
    }
  }

  private def getAuthenticationType(r: PortletRequest) = {
    val authType = r.getParameter("authType")
    if(authType == null || authType.isEmpty) AuthenticationType.Basic
    else AuthenticationType.withName(authType)
  }

  private def getAuthorizationScopes(r: PortletRequest): AuthorizationScope.ValueSet = {
    val scopes = r.getParameterValues("scope")
    if(scopes == null || scopes.isEmpty) AuthorizationScope.ValueSet.empty
    else AuthorizationScope.ValueSet.apply(
      scopes.map { scope => AuthorizationScope.withName(scope) }: _*)
  }

  override def processAction(request: ActionRequest, response: ActionResponse): Unit = {
    val view = getShowView(request)
    val act  = getAction(request)

    act match {
      case Some(Action.Add) =>
        val appName = request.getParameter("appName")
        val appDesc = request.getParameter("appDesc")
        val authType = getAuthenticationType(request)
        val scope    = getAuthorizationScopes(request)
        authentication.RegistrationApp(appName, appDesc.toOption, scope, authType)

      case Some(Action.Edit) =>
        val appId   = request.getParameter("appId")
        val appName = request.getParameter("appName")
        val appDesc = request.getParameter("appDesc")
        val authType = getAuthenticationType(request)
        val scope    = getAuthorizationScopes(request)
        authentication.UpdateApplication(appId, appName, Some(appDesc), scope, authType)

      case Some(Action.Delete) =>
        val appId = request.getParameter("appId")
        authentication.DeleteApplication(appId)

      case Some(Action.Block) =>
        val appId     = request.getParameter("appId")
        authentication.BlockApplication(appId)

      case Some(Action.Unblock) =>
        val appId     = request.getParameter("appId")
        authentication.UnblockApplication(appId)

      case _ =>
    }

    view match {
      case View.List =>
        response.setRenderParameter("offset"   , getOffset   (request))
        response.setRenderParameter("appsCount", getAppsCount(request))
        response.setRenderParameter(View.Name  , View.List.toString)

      case View.Add =>
        response.setRenderParameter(View.Name, View.Add.toString)

      case View.Edit =>
        response.setRenderParameter("appId", request.getParameter("appId"))
        response.setRenderParameter(View.Name , View.Edit.toString)
    }
  }

  override def doView(request: RenderRequest, response: RenderResponse) {
    val offset      = getOffset   (request).toInt
    val appsCount   = getAppsCount(request).toInt
    val showView    = getShowView (request)

    showView match {

      case View.Add =>
        val html = edit(new AppAddOrEditPortletView()(request, response))
        response.getWriter.write(index(html) toString)

      case View.Edit =>
        val result = getSelectedApp(request)
        val html = edit(new AppAddOrEditPortletView(result)(request, response))
        response.getWriter.write(index(html) toString)

      case _ =>

        val result = authentication.GetApplications(appsCount, offset)
        val html = list(new AppListPortletView(result)(request, response))
        response.getWriter.write(index(html) toString)
    }
  }
}



