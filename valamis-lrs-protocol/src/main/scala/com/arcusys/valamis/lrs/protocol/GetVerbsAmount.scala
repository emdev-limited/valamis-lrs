package com.arcusys.valamis.lrs.protocol

import com.arcusys.valamis.lrs.tincan.Verb
import org.joda.time.DateTime

/**
 * Created by Iliya Tryapitsin on 07.08.15.
 */
case class GetVerbsAmount(since:  Option[DateTime] = None,
                          verbId: Option[Verb#Id]  = None) extends Message
