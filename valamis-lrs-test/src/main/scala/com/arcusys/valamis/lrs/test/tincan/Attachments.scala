package com.arcusys.valamis.lrs.test.tincan

/**
 * Created by Iliya Tryapitsin on 13/02/15.
 */

case class Attachment(statementMetadata: StatementMetadata,
                      content: Option[String] = None)

case class StatementMetadata(usageType: String,
                             display: Map[String, String],
                             contentType: String,
                             length: Int,
                             sha2: String,
                             fileUrl: Option[String] = None)

object Attachments {
  private val usageType = "http://id.tincanapi.com/attachment/supporting_media"

  object Good {
    val `should pass text attachment` = text

    val `should pass json attachment` = Attachment(
      StatementMetadata(usageType, LanguageMaps.good3.get, "application/json", 60, "f4135c31e2710764604195dfe4e225884d8108467cc21670803e384b80df88ee"),
      Some("{\"propertyA\":\"value1\",\"propertyB\":\"value2\",\"propertyC\":true}"))

    val `should pass file url only attachment` = Attachment(
      StatementMetadata(usageType, LanguageMaps.good3.get, "application/octet-stream", 65556, "d14f1580a2cebb6f8d4a8a2fc0d13c67f970e84f8d15677a93ae95c9080df899", Some("http://tincanapi.com/conformancetest/attachment/fileUrlOnly")))
  }

  val text = Attachment(
    StatementMetadata(usageType, LanguageMaps.good3.get, "text/plain", 18, "bd1a58265d96a3d1981710dab8b1e1ed04a8d7557ea53ab0cf7b44c04fd01545"),
    Some("some text content"))

  val oneTextMetadata = Some(Seq(text.statementMetadata))
}