package com.arcusys.valamis.lrs.jdbc.database.row

import com.arcusys.valamis.lrs.security.AuthenticationType
import com.arcusys.valamis.lrs.tincan._
import org.joda.time.DateTime

/**
 * Created by Iliya Tryapitsin on 22.04.15.
 * Created by Iliya Tryapitsin on 22.04.15.
 */
case class ApplicationRow(appId      :ApplicationRow#Key,
                          name       :String,
                          description:String,
                          appSecret  :String,
                          scope      :AuthorizationScope.ValueSet,
                          regDateTime:DateTime,
                          isActive   :Boolean = true,
                          authType   :AuthenticationType.Type) {
  type Key = String
}