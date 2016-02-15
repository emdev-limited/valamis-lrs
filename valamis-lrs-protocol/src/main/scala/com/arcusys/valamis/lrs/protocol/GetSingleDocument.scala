package com.arcusys.valamis.lrs.protocol

import com.arcusys.valamis.lrs.tincan.{Activity, ProfileId}

/**
 * Created by Iliya Tryapitsin on 07.08.15.
 */
case class GetSingleDocument(activityId: Activity#Id,
                             profileId:  ProfileId) extends Message
