package com.arcusys.valamis.lrs.test.tincan

/**
 * Created by Iliya Tryapitsin on 13/02/15.
 */
object Contents {
  val json = """{
               |   test: "JSON content",
               |   obj: {
               |        subObj: {
               |              nested: "content"
               |        },
               |        arr: [0, 1, "str"]
               |   },
               |   arr: [1.3, "item", 3.1]
               |}""".mkString

  val string = "some string of content"
  val emptyString = ""
  val languageMap = Map("en-US" -> "Test Language Map")
}
