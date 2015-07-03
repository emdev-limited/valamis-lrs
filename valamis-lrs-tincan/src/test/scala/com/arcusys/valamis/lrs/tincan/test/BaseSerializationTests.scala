package com.arcusys.valamis.lrs.tincan.test

import org.json4s.{DefaultFormats, Formats}
import org.scalatest.{BeforeAndAfterEach, FlatSpec, Matchers}

import scala.io.Source

/**
 * Created by Iliya Tryapitsin on 30/12/14.
 */
abstract class BaseSerializationTests extends FlatSpec with Matchers with BeforeAndAfterEach {
  implicit val jsonFormats: Formats = DefaultFormats

  def loadDataFromTxtResource(path: String) = Source.fromURL(getClass.getResource(path)).getLines().mkString

}

