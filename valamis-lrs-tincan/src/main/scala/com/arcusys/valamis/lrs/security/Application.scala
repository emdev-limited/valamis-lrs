package com.arcusys.valamis.lrs.security

import com.arcusys.valamis.lrs.tincan._
import org.joda.time.DateTime

/**
 * Oauth and Basic consumer application
 */
case class Application(appId     :Application#Id,
                      name       :String,
                      description:String,
                      appSecret  :String,
                      scope      :AuthorizationScope.ValueSet,
                      regDateTime:DateTime,
                      isActive   :Boolean = true,
                      authType   :AuthenticationType.Type) {
  type Id = String
}