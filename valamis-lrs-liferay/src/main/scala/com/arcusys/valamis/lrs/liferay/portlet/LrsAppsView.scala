package com.arcusys.valamis.lrs.liferay.portlet

import javax.portlet._

import com.arcusys.valamis.lrs._
import com.arcusys.valamis.lrs.jdbc.SecurityManager
import com.arcusys.valamis.lrs.liferay.{LrsTypeLocator, LrsModule}
import com.arcusys.valamis.lrs.security.AuthenticationType
import com.arcusys.valamis.lrs.tincan.AuthorizationScope
import com.google.inject._
import com.google.inject.name.Names
import html.apps.html._

/**
 * Created by Iliya Tryapitsin on 21.04.15.
 */
class LrsAppsView extends GenericPortlet with LrsTypeLocator {

  lazy val injector        = Guice.createInjector(new LrsModule)
  lazy val securityManager = injector.getInstance(Key.get(classOf[SecurityManager], Names.named(LrsType.Simple.toString)))

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
      case Some(id) => securityManager.getApplication(id)

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
        securityManager.registrationApp(appName, appDesc.toOption, scope, authType)

      case Some(Action.Edit) =>
        val appId   = request.getParameter("appId")
        val appName = request.getParameter("appName")
        val appDesc = request.getParameter("appDesc")
        val authType = getAuthenticationType(request)
        val scope    = getAuthorizationScopes(request)
        securityManager.updateApplication(appId, appName, Some(appDesc), scope, authType)

      case Some(Action.Delete) =>
        val appId = request.getParameter("appId")
        securityManager.deleteApplication(appId)

      case Some(Action.Block) =>
        val appId     = request.getParameter("appId")
        securityManager.blockApplication(appId)

      case Some(Action.Unblock) =>
        val appId     = request.getParameter("appId")
        securityManager.unblockApplication(appId)

      case Some(Action.LrsTypeChanged) =>
        val tpe           = request.getParameter("lrsType")
        val msgBusAddress = request.getParameter("msgBus" )

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
        val html = edit(new AppAddOrEditPortletViewModel()(request, response))
        response.getWriter.write(index(html) toString)

      case View.Edit =>
        val result = getSelectedApp(request)
        val html = edit(new AppAddOrEditPortletViewModel(result)(request, response))
        response.getWriter.write(index(html) toString)

      case _ =>
        val result = securityManager.getApplications(appsCount, offset)
        val clientApiListView   = clientApiList(
          new AppListPortletViewModel(result, LrsType.Simple)(request, response)
        )
        response.getWriter.write(index(clientApiListView) toString)
    }
  }
}