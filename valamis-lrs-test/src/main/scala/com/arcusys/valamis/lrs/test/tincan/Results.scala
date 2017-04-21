package com.arcusys.valamis.lrs.test.tincan

import com.arcusys.valamis.lrs.utils._

/**
 * Created by Iliya Tryapitsin on 12/02/15.
 */
case class Result(score: Option[Score] = None,
                  success: Option[Boolean] = None,
                  completion: Option[Boolean] = None,
                  response: Option[String] = None,
                  duration: Option[String] = None,
                  extensions: Option[Map[String, Any]] = None)

object Results {

  val success = Some(true)
  val completion = Some(true)
  val response = Some("test")
  val duration = Some("PT2H")

  val noExtensions = Some(Result(Scores.allProperties, success, completion, response, duration))
  val empty = Some(Result())

  object Good {
    val `should pass empty result`                      = empty
    val `should pass result with scores only`           = Result(score = Scores.typical.toOption)
    val `should pass result with empty scores only`     = Result(score = Scores.empty.toOption)
    val `should pass result with success only`          = Result(success = success)
    val `should pass result with completion only`       = Result(completion = completion)
    val `should pass result with response only`         = Result(response = response)
    val `should pass result with duration only`         = Result(duration = duration)
    val `should pass result with extensions only`       = Result(extensions = Extensions.multiplePairs)
    val `should pass result with empty extensions only` = Result(extensions = Extensions.empty)
    val `should pass result with score & success`     = Result(score = Scores.typical.toOption, success = success)
    val `should pass result with score & completion`  = Result(score = Scores.typical.toOption, completion = completion)
    val `should pass result with score & response`    = Result(score = Scores.typical.toOption, response = response)
    val `should pass result with score & duration`    = Result(score = Scores.typical.toOption, duration = duration)
    val `should pass result with success & completion` = Result(score = Scores.typical.toOption, success = success)
    val `should pass result with success & response`   = Result(score = Scores.typical.toOption, response = response)
    val `should pass result with success & duration`   = Result(score = Scores.typical.toOption, duration = duration)
    val `should pass result with success, completion & response` = Result(success = success, completion = completion, response = response)
    val `should pass result with success, completion & duration` = Result(success = success, completion = completion, duration = duration)
    val `should pass result with success, response & duration`   = Result(success = success, response = response, duration = duration)
    val `should pass result with success, completion, response & duration` = Result(success = success, completion = completion, response = response, duration = duration)
    val `should pass result with completion & response` = Result(completion = completion, response = response)
    val `should pass result with response & duration`   = Result(response = response, duration = duration)
    val `should pass result with response, completion & response` = Result(response = response, completion = completion, duration = duration)
    val `should pass result with score, success & completion` = Result(score = Scores.typical.toOption, success = success, completion = completion)
    val `should pass result with score, success & response`   = Result(score = Scores.typical.toOption, success = success, response = response)
    val `should pass result with score, success & duration`   = Result(score = Scores.typical.toOption, success = success, duration = duration)
    val `should pass result with score, completion & response` = Result(score = Scores.typical.toOption, completion = completion, response = response)
    val `should pass result with score, completion & duration` = Result(score = Scores.typical.toOption, completion = completion, duration = duration)
    val `should pass result with score, response & duration`   = Result(score = Scores.typical.toOption, response = response, duration = duration)
    val `should pass result with score, success, completion & response` = Result(score = Scores.typical.toOption, success = success, completion = completion, response = response)
    val `should pass result with score, success, completion & duration` = Result(score = Scores.typical.toOption, success = success, completion = completion, duration = duration)
    val `should pass result with all properties`               = Result(success = success, completion = completion, response = response, duration = duration, extensions = Extensions.typical)
  }
}
