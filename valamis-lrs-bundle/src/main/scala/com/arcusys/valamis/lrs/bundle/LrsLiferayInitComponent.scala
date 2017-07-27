package com.arcusys.valamis.lrs.bundle

import com.arcusys.valamis.lrs.liferay.message.LrsRegistrator
import com.arcusys.valamis.lrs.liferay.{Loggable, LrsModeInitializer}
import com.liferay.portal.kernel.messaging.MessageBus
import com.liferay.portal.kernel.model.Release
import com.liferay.portal.kernel.module.framework.ModuleServiceLifecycle
import org.osgi.framework.BundleContext
import org.osgi.service.component.annotations.{Activate, Component, Modified, Reference}

/**
  * 'init' method of this component will be invoked when
  * all needed for LRS initialization stuff is ready
  */
@Component(immediate = true, service = Array())
class LrsLiferayInitComponent extends Loggable {

  @Activate
  @Modified
  protected def init(bundleContext: BundleContext, props: java.util.Map[String, Object]): Unit = {
    logger.info("LrsLiferayInitComponent.init()")
    LrsModeInitializer.init()
    LrsRegistrator.sendStartupMessage()
  }

  @Reference(target = ModuleServiceLifecycle.PORTAL_INITIALIZED, unbind = "-")
  protected def setModuleServiceLifecycle(moduleServiceLifecycle: ModuleServiceLifecycle) {}

  @Reference(unbind = "-")
  protected def setMessageBus(mBus: MessageBus) {}

  @Reference(target = "(&(release.bundle.symbolic.name=com.arcusys.valamis.lrs.bundle)" +
    "(release.schema.version>=300))")
  protected def setRelease(release: Release) {}

}
