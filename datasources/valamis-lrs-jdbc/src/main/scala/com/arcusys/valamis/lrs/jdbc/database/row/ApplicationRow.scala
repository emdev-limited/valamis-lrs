package com.arcusys.valamis.lrs.jdbc.database.row

import com.arcusys.valamis.lrs.security.AuthenticationType
import com.arcusys.valamis.lrs.tincan._
import org.joda.time.DateTime

case class ApplicationRow(appId      :ApplicationRow#Key,
                          name       :String,
                          description:Option[String],
                          appSecret  :String,
                          scope      :AuthorizationScope.ValueSet,
                          regDateTime:DateTime,
                          isActive   :Boolean = true,
                          authType   :AuthenticationType.Type) {
  type Key = String
}