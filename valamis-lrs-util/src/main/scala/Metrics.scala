package com.arcusys.valamis.lrs

import com.codahale.metrics.MetricRegistry
import nl.grons.metrics.scala.InstrumentedBuilder

/**
 * Created by Iliya Tryapitsin on 11.08.15.
 */
object Metrics {
  val metricRegistry = new MetricRegistry()
}

trait Instrumented extends InstrumentedBuilder {
  val metricRegistry = Metrics.metricRegistry
}