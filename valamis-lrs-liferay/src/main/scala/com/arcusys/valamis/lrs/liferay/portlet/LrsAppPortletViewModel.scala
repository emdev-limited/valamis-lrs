package com.arcusys.valamis.lrs.liferay.portlet

import javax.portlet.{RenderResponse, RenderRequest}
import com.arcusys.valamis.lrs.LrsType
import com.arcusys.valamis.lrs.security.{Application, AuthenticationType}
import com.arcusys.valamis.lrs.tincan.AuthorizationScope
import com.arcusys.valamis.lrs.tincan.AuthorizationScope._

/**
 * Created by Iliya Tryapitsin on 26.04.15.
 */
class AppListPortletViewModel(val apps:    Seq[Application])
                             (implicit request: RenderRequest,
                         response: RenderResponse) extends BasePortletView(request, response) {
}

class AppAddOrEditPortletViewModel(val selectedApp: Option[Application] = None)
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