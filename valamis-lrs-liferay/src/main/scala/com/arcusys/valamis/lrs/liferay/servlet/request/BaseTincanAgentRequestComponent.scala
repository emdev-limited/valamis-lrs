package com.arcusys.valamis.lrs.liferay.servlet.request

import com.arcusys.valamis.lrs.serializer.AgentSerializer
import com.arcusys.valamis.lrs.tincan.Agent
import com.arcusys.json.JsonHelper
import BaseTincanAgentRequestComponent.{ Agent => a }

/**
  * Created by Iliya Tryapitsin on 29/12/14.
 */

trait BaseTincanAgentRequestComponent {
  r: BaseLrsRequest =>

  def agent(implicit m: Manifest[Agent]) = JsonHelper.fromJson[Agent](require(a), new AgentSerializer)
}

object BaseTincanAgentRequestComponent {
  val Agent = "agent"
}