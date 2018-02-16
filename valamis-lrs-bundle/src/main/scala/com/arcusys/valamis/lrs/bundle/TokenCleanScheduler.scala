package com.arcusys.valamis.lrs.bundle

import com.arcusys.valamis.lrs.liferay.util.TokenCleanSupport
import com.liferay.portal.kernel.messaging.{BaseSchedulerEntryMessageListener, DestinationNames, Message}
import com.liferay.portal.kernel.module.framework.ModuleServiceLifecycle
import com.liferay.portal.kernel.scheduler.{SchedulerEngineHelper, TimeUnit, TriggerFactory, TriggerFactoryUtil}
import org.osgi.service.component.annotations._

/**
  * Created by pkornilov on 10.08.16.
  *
  * This is OSGi replacement of <scheduler-entry> in liferay-portlet.xml for LrsClients portlet
  *
  */
@Component(immediate = true, service = Array(classOf[TokenCleanScheduler]))
class TokenCleanScheduler extends BaseSchedulerEntryMessageListener with TokenCleanSupport {

  private var _schedulerEngineHelper: SchedulerEngineHelper = _ //to be injected by Service Component Runtime

  override def doReceive(message: Message): Unit = {
    cleanExpiredTokens()
  }

  @Activate
  @Modified
  protected def activate(properties: java.util.Map[String, Object]) {

    schedulerEntryImpl.setTrigger(
      TriggerFactoryUtil.createTrigger(
        getEventListenerClass, getEventListenerClass,
        1, TimeUnit.DAY))

    _schedulerEngineHelper.register(
      this, schedulerEntryImpl, DestinationNames.SCHEDULER_DISPATCH)
  }

  @Deactivate
  protected def deactivate() {
    _schedulerEngineHelper.unregister(this)
  }

  @Reference(unbind = "-")
  protected def setSchedulerEngineHelper(schedulerEngineHelper: SchedulerEngineHelper) {
    _schedulerEngineHelper = schedulerEngineHelper
  }

  //These methods are needed to make sure,
  //that needed Liferay services have been initialized before activating our component
  @Reference(target = ModuleServiceLifecycle.PORTAL_INITIALIZED, unbind = "-")
  protected def setModuleServiceLifecycle(moduleServiceLifecycle: ModuleServiceLifecycle) {}

  @Reference(unbind = "-")
  protected def setTriggerFactory(triggerFactory: TriggerFactory) {}

}
