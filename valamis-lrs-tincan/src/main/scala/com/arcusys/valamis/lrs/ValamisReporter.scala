package com.arcusys.valamis.lrs

import java.net.URI

import com.arcusys.valamis.lrs.tincan._
import com.arcusys.valamis.lrs.tincan.valamis.ActivityIdLanguageMap
import org.joda.time.DateTime

/**
 * Custom Valamis LRS API
 */
trait ValamisReporter {

  /**
   * Get Tincan Verb amount since date time
   * @param start Start date time
   * @param verb Verb URI
   * @return Count of found verbs
   */
  def verbAmount(start: Option[DateTime] = None,
                 verb:  Option[URI] = None): Int

  /**
   * Get Tincan [[Verb]] amount since date time grouped by Tincan [[Verb.id]]
   * @param start Start date time
   * @return List of [[Verb.id]] and [[Verb]] count
   */
  def verbAmountByGroup(start: Option[DateTime]): Seq[(String, Int)]

  def verbIdsWithDate(start: Option[DateTime]): Seq[(String, DateTime)]

  /**
   * Find Tincan [[Verb]] with [[Activity.id]] and [[Activity.description]]
   * @param filter Filter for [[Verb.display]] and [[Activity.name]]
   * @param limit Maximum count
   * @param offset Offset from head
   * @param sortNameDesc Sort direction by name
   * @param sortTimeDesc Sort direction by time
   * @return List Tincan [[Verb]] with [[Activity.id]] and [[Activity.description]]
   */
  def verbWithActivities(filter:  Option[String] = None,
                         limit:   Int            = 10,
                         offset:  Int            = 0,
                         sortNameDesc: Boolean   = true,
                         sortTimeDesc: Boolean   = false,
                         sortTimeFirst: Boolean  = false): SeqWithCount[(Verb, ActivityIdLanguageMap, Option[DateTime])]

  def findMaxActivityScaled (actor:  Actor,
                             verbId: Verb#Id): Seq[(String, Option[Float])]

  def findStatementsCount (agent: Agent,
                           verbs: Seq[URI]): Int

  def findMinDate (agent:       Agent,
                   verbs:       Seq[URI],
                   activityIds: Seq[URI],
                   since:       DateTime): Seq[(URI, Activity#Id, DateTime)]
}
