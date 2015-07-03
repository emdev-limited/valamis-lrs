package com.arcusys.valamis.lrs.liferay.servlet.request

/**
 * Created by Iliya Tryapitsin on 29/12/14.
 */

object TincanBaseProfileRequest {
  val PROFILE_ID = "profileId"
}

trait BaseTincanProfileRequestComponent {
  r: BaseLrsRequest =>

  import com.arcusys.valamis.lrs.liferay.servlet.request.TincanBaseProfileRequest._

  def hasProfileId = has(PROFILE_ID)

  def profileId = optional(PROFILE_ID)
}
