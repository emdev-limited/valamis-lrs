package com.arcusys.valamis.lrs.liferay.servlet

import net.oauth.{OAuth => oa}

/**
 * Created by Iliya Tryapitsin on 29.04.15.
 */
package object oauth {
  val HmacSha1               = oa.HMAC_SHA1
  val RsaSha1                = oa.RSA_SHA1
  val Encoding               = oa.ENCODING
  val Version10              = oa.VERSION_1_0
  val OAuthToken             = oa.OAUTH_TOKEN
  val OAuthNonce             = oa.OAUTH_NONCE
  val FormEncoded            = oa.FORM_ENCODED
  val OAuthVersion           = oa.OAUTH_VERSION
  val OAuthVerifier          = oa.OAUTH_VERIFIER
  val OAuthCallback          = oa.OAUTH_CALLBACK
  val OAuthSignature         = oa.OAUTH_SIGNATURE
  val OAuthTimestamp         = oa.OAUTH_TIMESTAMP
  val OAuthConsumerKey       = oa.OAUTH_CONSUMER_KEY
  val OAuthTokenSecret       = oa.OAUTH_TOKEN_SECRET
  val OAuthSignatureMethod   = oa.OAUTH_SIGNATURE_METHOD
  val OAuthCallbackConfirmed = oa.OAUTH_CALLBACK_CONFIRMED

  val TokenExpired           = "token_expired"
  val Authorized             = "authorized"
  val User                   = "user"
  val PermissionDenied       = "permission_denied"
  val ScopeParameter         = "scope"
  val ScopeFailed            = "scope_failed"
  val ConsumerFailed         = "consumer_failed"

}
