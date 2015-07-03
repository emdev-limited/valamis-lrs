package com.arcusys.valamis.lrs.liferay.servlet.response

/**
 * Created by Iliya Tryapitsin on 27/12/14.
 */
case class GetAboutResponse(extensions: ExtensionResponse = ExtensionResponse(), version: Seq[String] = Seq("1.0.1"))

case class ExtensionResponse(`http://id.tincanapi.com/extension/powered-by`: ContactResponse = ContactResponse())

case class ContactResponse(name: String = "Valamis Tin Can Engine",
                           homePage: String = "http://www.valamislearning.com/",
                           version: String = "2.2.0")
