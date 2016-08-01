package com.arcusys.valamis.lrs.tincan

import java.util.UUID

/**
  * StateProfile structure
  */
case class StateProfile(
                       agent:        Agent,
                       activityId:   Activity#Id,
                       stateId:      ProfileId,
                       registration: Option[UUID],
                       document:     Document
                       ) {
  override def toString =
    s"""
       |ActivityProfile instance
       |agent       = $agent
       |activityId  = $activityId
       |stateId     = $stateId
       |registration= $registration
       |document    = $document
     """.stripMargin
}
