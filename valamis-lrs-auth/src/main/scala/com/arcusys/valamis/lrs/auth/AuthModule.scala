package com.arcusys.valamis.lrs.auth

import net.codingwell.scalaguice.ScalaModule

/**
 * Created by Iliya Tryapitsin on 17/03/15.
 */
class AuthModule extends ScalaModule {
  override def configure(): Unit = {
//    bind[Actor].annotatedWith(Names.named(AuthenticationActor.name)).to(classOf[AuthenticationActor])
    bind(classOf[Authentication])
  }
}
