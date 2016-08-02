package com.arcusys.valamis.lrs.tincan

/**
  * Activity profile structure
  */
case class ActivityProfile(
                       activityId:  Activity#Id,
                       profileId:   ProfileId,
                       document:    Document
                       ) {
  override def toString =
    s"""
       |ActivityProfile instance
       |profileId    = $profileId
       |activityId   = $activityId
       |document     = $document
     """.stripMargin
}
