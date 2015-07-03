package com.arcusys.valamis.lrs.auth

import com.arcusys.valamis.lrs.tincan._
import org.joda.time.DateTime

/**
 * Created by Iliya Tryapitsin on 22.04.15.
 */
case class Application(appId      :String,
                       name       :String,
                       description:String,
                       appSecret  :String,
                       scope      :AuthorizationScope.ValueSet,
                       regDateTime:DateTime,
                       isActive   :Boolean = true,
                       authType   :AuthenticationType.Type )