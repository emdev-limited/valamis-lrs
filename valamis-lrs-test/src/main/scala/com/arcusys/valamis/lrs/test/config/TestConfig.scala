package com.arcusys.valamis.lrs.test.config

/**
 * Created by Iliya Tryapitsin on 13/03/15.
 */

import com.typesafe.config._

import scala.collection.JavaConverters._

class TestConfig(resource: String,
                 path:     String) {

  val ref = ConfigFactory.parseResources(getClass, resource)
  val defaults = ref.getObject(path).toConfig

  def getStrings(config: Config, path: String): Option[Seq[String]] = {
    if (config.hasPath(path)) {
      config.getValue(path).unwrapped() match {
        case l: java.util.List[_] => Some(l.asScala.map(_.toString))
        case o                    => Some(List(o.toString))
      }
    } else None
  }

  def testConfig(name: String) = {
    val c = if (ref.hasPath(name)) ref.getConfig(name).withFallback(defaults) else defaults
    c.resolve()
  }
}

object TestConfig {
  def apply(resource: String,
            path:     String = "defaults") = new TestConfig(resource, path)
}