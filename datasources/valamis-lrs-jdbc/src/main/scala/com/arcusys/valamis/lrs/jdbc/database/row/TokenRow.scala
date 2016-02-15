package com.arcusys.valamis.lrs.jdbc.database.row

import org.joda.time.DateTime

/**
 * Created by iliyatryapitsin on 15.04.15.
 */
case class TokenRow(userKey:        Option[AgentRow#Type],
                    applicationKey: ApplicationRow#Key,
                    code:           String,
                    codeSecret:     String,
                    callback:       String,
                    issueAt:        DateTime,
                    verifier:       Option[String] = None,
                    token:          Option[String] = None,
                    tokenSecret:    Option[String] = None)