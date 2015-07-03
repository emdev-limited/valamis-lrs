package com.arcusys.valamis.lrs.tincan.test

import org.scalatest.FeatureSpec
import com.arcusys.valamis.lrs.serializer._

/**
 * Created by Iliya Tryapitsin on 20.05.15.
 */
class StringExtensionTests extends FeatureSpec {
  feature("Clean string") {
    val testString = ",, \t Hello, world! ,,, \n"
    val expectedResult = "Hello, world!"

    assert(testString.trimAll() == expectedResult)

    val optionTestString = Some(testString)
    assert(optionTestString.trimAll() == Some(expectedResult))

    assert(testString.trimAll(removeSymbols + "!") == expectedResult.substring(0, expectedResult.length - 1))
  }
}
