package com.arcusys.valamis.lrs

import java.net.URI

import com.arcusys.valamis.lrs.security.{Application, AuthenticationType, Token}
import com.arcusys.valamis.lrs.tincan._
import com.arcusys.valamis.lrs.tincan.valamis.ActivityIdLanguageMap
import org.joda.time.DateTime

trait ValamisReporterComponent {

  val statementStorage: StatementStorage

  trait StatementStorage {


    def verbAmount(start: Option[DateTime] = None,
                   verb:  Option[URI] = None): Int


    def verbAmountByGroup(start: Option[DateTime]): Seq[(String, Int)]

    def verbIdsWithDate(start: Option[DateTime]): Seq[(String, DateTime)]

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

}