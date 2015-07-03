package com.arcusys.valamis.lrs.liferay.portlet

import javax.portlet.{RenderResponse, RenderRequest}
import com.arcusys.valamis.lrs.auth._
import com.arcusys.valamis.lrs.tincan.AuthorizationScope
import com.arcusys.valamis.lrs.tincan.AuthorizationScope._

/**
 * Created by Iliya Tryapitsin on 26.04.15.
 */
class AppListPortletView(val apps:   Seq[Application])
                        (implicit request: RenderRequest,
                         response: RenderResponse) extends BasePortletView(request, response)

class AppAddOrEditPortletView(val selectedApp: Option[Application] = None)
                             (implicit request: RenderRequest,
                              response: RenderResponse) extends BasePortletView(request, response) {
  def action = selectedApp match {
    case None    => Action.Add
    case Some(_) => Action.Edit
  }

  def isSelectedScope(scope: AuthorizationScope.Type) = selectedApp match {
    case Some(v) => if(v.scope <== scope.toValueSet) "checked" else ""
    case None => ""
  }

  def isSelectedAuthType(authType: AuthenticationType.Type) = selectedApp match {
    case Some(v) => if(v.authType == authType) "selected" else ""
    case None => ""
  }
}