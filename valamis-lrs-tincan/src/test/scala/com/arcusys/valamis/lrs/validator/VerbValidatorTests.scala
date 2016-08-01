package com.arcusys.valamis.lrs.validator

import com.arcusys.valamis.lrs.exception.VerbInvalidException
import com.arcusys.valamis.lrs.tincan.Verb
import org.scalatest.{BeforeAndAfterEach, Matchers, FlatSpec}

/**
 * Created by eboystova on 15.01.16.
 */
class VerbValidatorTests extends FlatSpec with Matchers with BeforeAndAfterEach {
  behavior of "Verb validator testing"

  it should "throw VerbInvalidException on empty verb" in {
    intercept[VerbInvalidException] {
      new Verb("", Map())
    }
  }

  it should "throw VerbInvalidException on 'Test' id" in {
    intercept[VerbInvalidException] {
       new Verb( "Test", Map("en-US" -> "experienced"))
    }
  }

  it should "throw VerbInvalidException on 'http://adlnet.gov/expapi/ verbs/experienced' id" in {
    intercept[VerbInvalidException] {
       new Verb( "http://adlnet.gov/expapi/ verbs/experienced", Map("en-US" -> "experienced"))
    }
  }


  it should "allow empty LanguageMap" in {
    new Verb("http://adlnet.gov/expapi/verbs/answered", Map())
  }


  it should "allow 'http://localhost' id" in {
    new Verb("http://localhost", Map("en-US" -> "experienced"))
  }

  it should "allow correct verb 'http://adlnet.gov/expapi/verbs/experienced'" in {
    val verb = new Verb("http://adlnet.gov/expapi/verbs/experienced",
      Map("en-US" -> "experienced"))
  }

  it should "allow correct verb 'http://adlnet.gov/expapi/verbs/answered'" in {
    val verb = new Verb("http://adlnet.gov/expapi/verbs/answered",
      Map("en-US" -> "answered"))
  }

  it should "allow correct verb tag:adlnet.gov,2013:expapi:0.9:activities:non-absolute-activity-id" in {
    val verb = new Verb("tag:adlnet.gov,2013:expapi:0.9:activities:non-absolute-activity-id",
      Map("en-US" -> "answered"))
  }

  it should "allow correct verb 'http://adlnet.gov/expapi/verbs/answered/että'" in {
    val verb = new Verb("http://adlnet.gov/expapi/verbs/answered/että",
      Map("en-US" -> "answered"))
  }

}
