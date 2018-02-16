package com.arcusys.valamis.lrs.bundle

import javax.servlet._

import com.codahale.metrics.health.HealthCheckRegistry
import com.codahale.metrics.{MetricFilter, MetricRegistry}
import com.codahale.metrics.servlets.MetricsServlet._
import com.codahale.metrics.servlets.HealthCheckServlet._

import com.arcusys.valamis.lrs.liferay.Loggable

/**
  * Created by pkornilov on 03.08.16.
  *
  * This filter is needed for Liferay 7 (OSGi) as a replacement for
  * <listener>
        <listener-class>com.arcusys.valamis.lrs.liferay.servlet.LrsHealthCheckServletContextListener</listener-class>
    </listener>
    <listener>
        <listener-class>com.arcusys.valamis.lrs.liferay.servlet.LrsServletContextListener</listener-class>
    </listener>

  from web.xml

  The reason is that ServletContextListeners are not supported in OSGi and only place I've found to set
  ServletContext attributes is 'init' method of Filter
  */
class MetricsInitializer extends Filter with Loggable {

  override def init(filterConfig: FilterConfig): Unit = {
    logger.info("Init metrics...")

    val context: ServletContext = filterConfig.getServletContext

    //attributes from MetricsServlet.ServletContextListener
    context.setAttribute(METRICS_REGISTRY, new MetricRegistry())
    context.setAttribute(METRIC_FILTER, MetricFilter.ALL)

    //attributes from HealthCheckServlet.ContextListener
    context.setAttribute(HEALTH_CHECK_REGISTRY, new HealthCheckRegistry())

  }

  override def doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain): Unit = {
    chain.doFilter(request, response)//just call next filter from the chain
  }

  override def destroy(): Unit = {}//nothing to do
}
