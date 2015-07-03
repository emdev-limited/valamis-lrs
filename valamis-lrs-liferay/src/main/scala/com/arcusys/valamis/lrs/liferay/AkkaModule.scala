package com.arcusys.valamis.lrs.liferay

import javax.inject.Inject

import akka.actor.ActorSystem
import com.arcusys.valamis.lrs.GuiceAkkaExtension
import com.arcusys.valamis.lrs.liferay.AkkaModule.ActorSystemProvider
import com.google.inject.{AbstractModule, Injector, Provider}
import com.typesafe.config._
import net.codingwell.scalaguice.ScalaModule

object AkkaModule {
  class ActorSystemProvider @Inject() (val config:   Config,
                                       val injector: Injector) extends Provider[ActorSystem] {
    override def get() = {
      val newConfig = config
        .withValue("akka.daemonic",
          ConfigValueFactory.fromAnyRef("on"))
        .withValue("akka.actor.default-dispatcher.executor",
          ConfigValueFactory.fromAnyRef("thread-pool-executor"))
      val system = ActorSystem("main-actor-system", newConfig)
      // add the GuiceAkkaExtension to the system, and initialize it with the Guice injector
      GuiceAkkaExtension(system).initialize(injector)
      system
    }
  }
}

/**
 * A module providing an Akka ActorSystem.
 */
class AkkaModule extends AbstractModule with ScalaModule {

  override def configure() {
    bind[ActorSystem].toProvider[ActorSystemProvider].asEagerSingleton()
  }
}