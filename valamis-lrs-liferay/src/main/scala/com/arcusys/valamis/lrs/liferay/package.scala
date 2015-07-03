package com.arcusys.valamis.lrs

import javax.servlet.http.{HttpServletResponse => r}

import com.liferay.portal.kernel.servlet.{HttpHeaders => h}

/**
 * Created by Iliya Tryapitsin on 21.04.15.
 */
package object liferay {
  type UpgradeProcess = com.liferay.portal.kernel.upgrade.UpgradeProcess
  type LogFactoryUtil = com.liferay.portal.kernel.log.LogFactoryUtil

  val XExperienceAPIConsistentThrough = "X-Experience-API-Consistent-Through"
  val AccessControlExposeHeaders      = "Access-Control-Expose-Headers"
  val AccessControlAllowMethods       = "Access-Control-Allow-Methods"
  val AccessControlAllowHeaders       = "Access-Control-Allow-Headers"
  val AccessControlAllowOrigin        = "Access-Control-Allow-Origin"
  val XExperienceAPIVersion           = "X-Experience-API-Version"

  val ContentType                     = h.CONTENT_TYPE
  val UserAgent                       = h.USER_AGENT
  val ContentLength                   = h.CONTENT_LENGTH
  val Authorization                   = h.AUTHORIZATION
  val ETag                            = h.ETAG
  val LastModified                    = h.LAST_MODIFIED
  val CacheControl                    = h.CACHE_CONTROL
  val Expires                         = h.EXPIRES
  val WwwAuthenticate                 = h.WWW_AUTHENTICATE
  val Location                        = h.LOCATION

  val Scope                           = "scope"

  val Never                           = "-1"
  val All                             = "*"

  val Head                            = "HEAD"
  val Get                             = "GET"
  val Post                            = "POST"
  val Put                             = "PUT"
  val Delete                          = "DELETE"
  val Options                         = "OPTIONS"

  val MustRevalidate                  = "must-revalidate"
  val NoCache                         = "no-cache"
  val NoStore                         = "no-store"

  val MovedTemporarily                = r.SC_MOVED_TEMPORARILY
  val Basic                           = "Basic "
}
