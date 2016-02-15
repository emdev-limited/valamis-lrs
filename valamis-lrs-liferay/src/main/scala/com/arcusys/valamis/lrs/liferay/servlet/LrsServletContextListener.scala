package com.arcusys.valamis.lrs.liferay.servlet

import com.codahale.metrics.MetricRegistry
import com.codahale.metrics.health.HealthCheckRegistry
import com.codahale.metrics.servlets.{MetricsServlet, HealthCheckServlet}

/**
 * Created by Iliya Tryapitsin on 11.08.15.
 */
class LrsServletContextListener extends MetricsServlet.ContextListener {

  val registry = new MetricRegistry()

  protected override def getMetricRegistry() = registry
}

class LrsHealthCheckServletContextListener extends HealthCheckServlet.ContextListener {

  val registry = new HealthCheckRegistry()

  protected override def getHealthCheckRegistry() = registry

}