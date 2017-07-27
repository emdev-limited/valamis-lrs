package com.arcusys.valamis.lrs.bundle

import javax.servlet.http.HttpServlet

import com.arcusys.valamis.lrs.liferay.{WebServletModule, lrsUrlPrefix}
import com.google.inject.Guice
import com.google.inject.servlet.GuiceFilter
import org.osgi.framework.{Bundle, BundleActivator, BundleContext}
import org.osgi.service.http.context.ServletContextHelper
import org.osgi.service.http.whiteboard.HttpWhiteboardConstants._


class DummyContext(val bundle: Bundle) extends ServletContextHelper(bundle)

class DummyServlet extends HttpServlet

class Activator extends BundleActivator {

  override def start(context: BundleContext) {
    activateServlets(context)
  }

  private def activateServlets(context: BundleContext): Unit = {
    implicit val bundleContext = context
    implicit val contextName = "valamis-lrs-portlet"

    registerServletContextHelper(new DummyContext(context.getBundle()))

    //TODO Metrics stuff are unstable under Liferay 7
    //and they are not really needed
    //so I commented it out (may be someday will restore them)

    //register MetricsInitializer as a filter to initialize attributes, needed for MetricsServlet
    //registerFilter(new MetricsInitializer(), s"$lrsUrlPrefix/*")

    //registerServlet(new AdminServlet(),"/metrics/*")

    registerFilter(new GuiceFilter(), s"$lrsUrlPrefix/*")
    Guice.createInjector(new WebServletModule)

    //register dummyServlet to force Equinox ProxyServlet to redirect "o/$contextName/xapi/*" requests
    //to our bundle. But, in fact, these requests will be intercepted and serviced by GuiceFilter, which, in its turn,
    //will use one of servlets, registered in WebServletModule
    registerServlet(new DummyServlet, s"$lrsUrlPrefix/*")
  }

  private def registerServletContextHelper(ctxHelper: ServletContextHelper)
                                          (implicit bundleContext: BundleContext, contextName: String): Unit = {
    val servletContextProps = new java.util.Hashtable[String, Object]
    servletContextProps.put(HTTP_WHITEBOARD_CONTEXT_NAME, s"$contextName")
    servletContextProps.put(HTTP_WHITEBOARD_CONTEXT_PATH, s"/$contextName")
    bundleContext.registerService(classOf[ServletContextHelper], ctxHelper, servletContextProps)
  }

  private def registerServlet(servlet: javax.servlet.Servlet, pattern: String)
                             (implicit bundleContext: BundleContext, contextName: String): Unit = {
    val servletProps = new java.util.Hashtable[String, Object]
    servletProps.put(HTTP_WHITEBOARD_SERVLET_PATTERN, pattern)
    servletProps.put(HTTP_WHITEBOARD_CONTEXT_SELECT, "(" + HTTP_WHITEBOARD_CONTEXT_NAME + s"=$contextName)")
    bundleContext.registerService(classOf[javax.servlet.Servlet], servlet, servletProps)
  }

  private def registerFilter(filter: javax.servlet.Filter, pattern: String)
                            (implicit bundleContext: BundleContext, contextName: String): Unit = {
    val filterProps = new java.util.Hashtable[String, Object]
    filterProps.put(HTTP_WHITEBOARD_FILTER_PATTERN, pattern)
    filterProps.put(HTTP_WHITEBOARD_CONTEXT_SELECT, "(" + HTTP_WHITEBOARD_CONTEXT_NAME + s"=$contextName)")
    bundleContext.registerService(classOf[javax.servlet.Filter], filter, filterProps)
  }

  override def stop(context: BundleContext) {

  }

}
