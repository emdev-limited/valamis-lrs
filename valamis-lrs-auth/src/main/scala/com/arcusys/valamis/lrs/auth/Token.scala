package com.arcusys.valamis.lrs.auth

import org.joda.time.DateTime

/**
 * Created by iliyatryapitsin on 15.04.15.
 */
case class Token(userKey:        Option[Long],
                 applicationKey: String,
                 code:           String,
                 codeSecret:     String,
                 callback:       String,
                 issueAt:        DateTime,
                 verifier:       Option[String] = None,
                 token:          Option[String] = None,
                 tokenSecret:    Option[String] = None)