package com.arcusys.valamis.lrs.liferay.servlet.request.valamis

import java.net.URI
import javax.servlet.http.HttpServletRequest

import com.arcusys.valamis.lrs.liferay.servlet.request._

/**
 * Created by Iliya Tryapitsin on 15.06.15.
 */
class ValamisVerbRequest(r: HttpServletRequest)
  extends BaseLrsRequest(r)
  with BaseTincanSinceRequestComponent
  with BaseTincanPartialRequestComponent
  with ValamisActionRequestComponent {

  def filter = optional(Filter)
  def verb = optional(Verb).map{s => new URI(s)}

  val Verb                = "verb"
  val Filter              = "filter"
  val VerbStatistics      = "verb-statistics"
  val VerbsWithActivities = "verb-with-activities"
  val VerbsAmount         = "verb-amount"
}
