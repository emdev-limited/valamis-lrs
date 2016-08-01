package com.arcusys.valamis.lrs.tincan

case class AgentProfile(profileId: ProfileId,
                        agent:     Agent,
                        content:   Document) {
  override def toString =
    s"""
       |AgentProfile instance
       |profileId   = $profileId
       |agent       = $agent
       |content     = $content
     """.stripMargin
}