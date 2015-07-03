package com.arcusys.valamis.lrs.test.tincan

/**
 * Created by Iliya Tryapitsin on 12/02/15.
 */
object Extensions {

  type Extension = Map[_ <: String, Object]

  val empty = Some(Map[String, Object]())
  val typical = Some(Map("http://id.tincanapi.com/extension/topic" -> "Conformance Testing"))
  val withObjectValue = Some(Map(
    "http://id.tincanapi.com/extension/color" -> Map(
      "model" -> "RGB",
      "value" -> "#FFFFFF")
  ))

  val withIntegerValue = Some(Map("http://id.tincanapi.com/extension/starting-position" -> 1))

  val multiplePairs = Some(Map(
    "http://id.tincanapi.com/extension/topic" -> "Conformance Testing",
    "http://id.tincanapi.com/extension/color" -> Map(
      "model" -> "RGB",
      "value" -> "#FFFFFF"
    ),
    "http://id.tincanapi.com/extension/starting-position" -> 1
  ))

  val invalidNonIRI = Some(Map("test" -> "key not an IRI"))

  val data = Map(
    "empty" -> Map[String, Object](),
    "typical" -> Map("http://id.tincanapi.com/extension/topic" -> "Conformance Testing"),

    "withObjectValue" -> Map(
      "http://id.tincanapi.com/extension/color" -> Map(
        "model" -> "RGB",
        "value" -> "#FFFFFF")
    ),

    "withIntegerValue" -> Map("http://id.tincanapi.com/extension/starting-position" -> 1),

    "multiplePairs" -> Map(
      "http://id.tincanapi.com/extension/topic" -> "Conformance Testing",
      "http://id.tincanapi.com/extension/color" -> Map(
        "model" -> "RGB",
        "value" -> "#FFFFFF"
      ),
      "http://id.tincanapi.com/extension/starting-position" -> 1
    ),

    "invalidNonIRI" -> Map("test" -> "key not an IRI")
  )

  def apply(key: String) = data.get(key)
}
