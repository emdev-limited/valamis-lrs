package com.arcusys.valamis.lrs.liferay.servlet.request

import javax.servlet.http.HttpServletRequest

/**
 * Created by Iliya Tryapitsin on 29/12/14.
 */

class TincanAgentProfileRequest(r: HttpServletRequest) extends BaseLrsRequest(r)
with BaseTincanAgentRequestComponent
with BaseTincanProfileRequestComponent
with BaseTincanSinceRequestComponent
with BaseTincanDocumentRequestComponent
