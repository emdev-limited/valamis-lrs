package com.arcusys.valamis.lrs.test.tincan

import org.json4s.{DefaultFormats, Formats}

/**
 * Created by iliyatryapitsin on 07.04.15.
 */
abstract class BaseSerializationTemplate[Good, Bad](good: Good, bad: Bad) {
  import Helper._

  implicit val jsonFormats: Formats = DefaultFormats
  val goodData  = good.fieldValues
  val badData   = bad.fieldValues

  def badTemplate(caseName: String)

  def goodTemplate(caseName: String)

  def run = {
    goodData foreach { x => goodTemplate(x._1) }
    badData foreach { x => badTemplate(x._1) }
  }
}
