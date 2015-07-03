package com.arcusys.valamis.lrs.liferay.servlet.request

import com.arcusys.valamis.lrs.serializer.AgentSerializer
import com.arcusys.valamis.lrs.tincan.Agent
import com.arcusys.valamis.utils.serialization.JsonHelper

/**
 * Created by Iliya Tryapitsin on 29/12/14.
 */

trait BaseTincanAgentRequestComponent {
  r: BaseLrsRequest =>

  import com.arcusys.valamis.lrs.liferay.servlet.request.BaseTincanAgentRequestComponent._

  def agent(implicit m: Manifest[Agent]) = JsonHelper.fromJson[Agent](require(AGENT), new AgentSerializer)

}

object BaseTincanAgentRequestComponent {
  val AGENT = "agent"
}