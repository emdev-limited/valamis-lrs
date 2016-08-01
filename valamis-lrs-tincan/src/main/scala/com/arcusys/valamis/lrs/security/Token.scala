package com.arcusys.valamis.lrs.security

import org.joda.time.DateTime

/**
 * Security OAuth token
 */
case class Token(userKey:        Option[Long],
                 applicationKey: Application#Id,
                 code:           String,
                 codeSecret:     String,
                 callback:       String,
                 issueAt:        DateTime,
                 verifier:       Option[String] = None,
                 token:          Option[String] = None,
                 tokenSecret:    Option[String] = None) {
}