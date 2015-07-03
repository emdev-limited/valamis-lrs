package com.arcusys.valamis.lrs.tincan.valamis

import org.joda.time.DateTime

/**
 * Created by Iliya Tryapitsin on 16.06.15.
 */
case class VerbStatistics(amount:       Int,
                          byGroup:      Seq[(String, Int)],
                          withDatetime: Seq[(String, DateTime)])