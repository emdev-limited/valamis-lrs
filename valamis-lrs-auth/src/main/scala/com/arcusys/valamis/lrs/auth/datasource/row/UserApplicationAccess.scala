package com.arcusys.valamis.lrs.auth.datasource.row

import org.joda.time.DateTime

/**
 * Created by iliyatryapitsin on 15.04.15.
 */
case class UserApplicationAccess(userKey:        Long,
                                 applicationKey: String,
                                 isAllow:        Boolean,
                                 createDateTime: DateTime,
                                 updateDateTime: DateTime)