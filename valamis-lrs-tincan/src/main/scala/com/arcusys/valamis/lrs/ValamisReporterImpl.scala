package com.arcusys.valamis.lrs

import java.net.URI

import com.arcusys.valamis.lrs.tincan._
import com.arcusys.valamis.lrs.tincan.valamis.ActivityIdLanguageMap
import org.joda.time.DateTime

trait ValamisReporterImpl extends ValamisReporter{
  this: ValamisReporterComponent =>


  def verbAmount(start: Option[DateTime] = None,
                 verb:  Option[URI] = None): Int = statementStorage.verbAmount(start, verb)


  def verbAmountByGroup(start: Option[DateTime]): Seq[(String, Int)] = statementStorage.verbAmountByGroup(start)

  def verbIdsWithDate(start: Option[DateTime]): Seq[(String, DateTime)] = statementStorage.verbIdsWithDate(start)

  def verbWithActivities(filter:  Option[String] = None,
                         limit:   Int            = 10,
                         offset:  Int            = 0,
                         sortNameDesc: Boolean   = true,
                         sortTimeDesc: Boolean   = false,
                         sortTimeFirst: Boolean  = false): SeqWithCount[(Verb, ActivityIdLanguageMap, Option[DateTime])] =
    statementStorage.verbWithActivities(filter, limit, offset, sortNameDesc, sortTimeDesc, sortTimeFirst)

  def findMaxActivityScaled (actor:  Actor,
                             verbId: Verb#Id): Seq[(String, Option[Float])] = statementStorage.findMaxActivityScaled(actor, verbId)

  def findStatementsCount (agent: Agent,
                           verbs: Seq[URI]): Int = statementStorage.findStatementsCount (agent, verbs)

  def findMinDate (agent:       Agent,
                   verbs:       Seq[URI],
                   activityIds: Seq[URI],
                   since:       DateTime): Seq[(URI, Activity#Id, DateTime)] =
    statementStorage.findMinDate(agent, verbs, activityIds, since)
}
