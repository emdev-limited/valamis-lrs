package com.arcusys.valamis.lrs.protocol

import com.arcusys.valamis.lrs.tincan.{Agent, Verb}

/**
 * Created by Iliya Tryapitsin on 07.08.15.
 */
case class GetActivityScaled(agent:  Agent,
                             verbId: Verb#Id) extends Message