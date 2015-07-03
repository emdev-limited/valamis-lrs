package com.arcusys.valamis.lrs.liferay.servlet.request

import javax.servlet.http.HttpServletRequest

/**
 * Created by Iliya Tryapitsin on 29/12/14.
 */

class TincanActivityProfileRequest(r: HttpServletRequest) extends BaseLrsRequest(r)
with BaseTincanActivityRequestComponent
with BaseTincanProfileRequestComponent