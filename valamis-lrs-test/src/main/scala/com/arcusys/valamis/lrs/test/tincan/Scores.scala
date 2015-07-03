package com.arcusys.valamis.lrs.test.tincan

/**
 * Created by Iliya Tryapitsin on 12/02/15.
 */
case class Score(scaled: Option[String] = None,
                 raw:    Option[String] = None,
                 min:    Option[String] = None,
                 max:    Option[String] = None)

object Scores {


  val scaled = Some(0.97.toString)
  val min    = Some(0.toString)
  val raw    = Some(100.toString)
  val max    = Some(100.toString)

  object Good {

  }

  val empty           = Score()
  val typical         = Score(scaled = scaled)
  val scaledOnly      = Score(scaled = scaled)
  val rawOnly         = Score(raw = raw)
  val minOnly         = Score(min = min)
  val maxOnly         = Score(max = max)
  val scaledAndRaw    = Score(scaled = scaled, raw = raw)
  val scaledAndMin    = Score(scaled = scaled, min = min)
  val scaledAndMax    = Score(scaled = scaled, max = max)
  val rawAndMin       = Score(raw = raw, min = min)
  val rawAndMax       = Score(raw = raw, max = max)
  val minAndMax       = Score(min = min, max = max)
  val scaledRawAndMin = Score(scaled = scaled, raw = raw, min = min)
  val scaledRawAndMax = Score(scaled = scaled, raw = raw, max = max)
  val rawMinAndMax    = Score(raw = raw, min = min, max = max)
  val allProperties   = Some(Score(scaled = scaled, raw = raw, min = min, max = max))
}
