import java.util.jar.{Attributes, Manifest => JarManifest}

import aQute.bnd.osgi.Analyzer
import aQute.lib.utf8properties.UTF8Properties
import sbt._


import com.arcusys.sbt.utils.OsgiHelper

object LrsOsgiHelper extends OsgiHelper {

  import com.arcusys.sbt.utils.CustomImportType._

  override val lpkgName = "ValamisLRS"
  override val osgiBundlesZip = "LrsBundles.zip"
  override val osgiDepsZip = "LrsDependencies.zip"

  override protected def fixVersion(attributes: Option[Attributes])(implicit props: UTF8Properties) = {
    val version = attributes.flatMap(a => Option(a.getValue("Implementation-Version")))
      .map(_.replace("-SNAPSHOT", ".SNAPSHOT"))
      .map(_.replace(".14-beta2", ".14.beta2"))
      .map(_.replace("1.7+r608262", "1.7"))
      .map(_.replace("3.5.1_a2.3", "3.5.1"))
      .map(_.replace("1.8.0_20", "1.8.0.20"))

    for (v <- version) props.put("Bundle-Version", v)
  }

  override protected val additionalHeaders = Map(
    "scala-guice_2.11-4.0.0.jar" -> ("Require-Bundle", "com.google.inject")
  )

  //structure of this map:
  //libFileName -> (Host Bundle-SymbolicName,Fragment Bundle-SymbolicName)
  override protected val fragments = Map(
    //fragments of json4s-core_2.11-3.2.11.jar (json4s-core)
    s"json4s-ast_2.11-${Version.json4s}.jar" ->("json4s-core", "json4s-ast"),
    //json4s-ext has its own package name and don't have to be a fragment

    //s"oauth-${Version.oauth}.jar" -> (s"oauth-${Version.oauth}.jar","oauth"),
    s"oauth-consumer-${Version.oauth}.jar" -> (s"oauth-${Version.oauth}.jar","oauth-consumer"),
    s"oauth-provider-${Version.oauth}.jar" -> (s"oauth-${Version.oauth}.jar","oauth-provider")
    //s"scala-guice_2.11-${Version.scalaGuice}.jar" -> ("com.google.inject","scala-guice")
  )

  override protected val customImportType = ByBundleName

  override protected val customExport = Map(
    "slick-migration" -> s"""com.arcusys.slick.migration.*;version="${Version.slickMigrations}",*""",
    "slick-drivers" -> s"""com.arcusys.slick.drivers.*;version="${Version.slickDrivers}",*""",
    "com.google.inject" -> "com.google.inject.internal,*")

  override protected val customImport = Map(
    "slick-migration" ->
      s"""
      |scala.slick.*;version="[${Version.slick},3)",
      |com.arcusys.slick.drivers.*;version="[${Version.slickDrivers},3)",*""".stripMargin,
    "slick-drivers" ->  s"""scala.slick.*;version="[${Version.slick},3)",*""",
    "Scala Guice" -> "!com.google.inject.internal,*"
  )

  override val exceptions = moduleFilter() - (
      "ch.qos.logback" % "logback-classic" |
      "ch.qos.logback" % "logback-core" |
      "org.slf4j" % "slf4j-api" |
      "org.apache.hadoop" % "hadoop-client" |
      "org.apache.hadoop" % "hadoop-common" |
      "org.apache.hadoop" % "hadoop-annotations" |
      "log4j" % "log4j" |
      "org.apache.hadoop" % "hadoop-auth" |
      "org.apache.hadoop" % "hadoop-hdfs" |
      "org.apache.hadoop" % "hadoop-mapreduce-client-app" |
      "org.apache.hadoop" % "hadoop-mapreduce-client-common" |
      "org.apache.hadoop" % "hadoop-yarn-common" |
      "org.apache.hadoop" % "hadoop-yarn-api" |
      "org.slf4j" % "slf4j-log4j12" |
      "org.apache.hadoop" % "hadoop-yarn-client" |
      "org.apache.hadoop" % "hadoop-mapreduce-client-core" |
      "org.apache.hadoop" % "hadoop-yarn-server-common" |
      "org.apache.hadoop" % "hadoop-mapreduce-client-shuffle" |
      "org.apache.hadoop" % "hadoop-mapreduce-client-jobclient" |
      "org.apache.zookeeper" % "zookeeper" |
      "com.typesafe.akka" % "akka-remote_2.11" |
      "com.typesafe.akka" % "akka-slf4j_2.11" |
      "org.slf4j" % "slf4j-api" |
      "org.apache.hadoop" % "hadoop-mapreduce-client-app" |
      "org.apache.hadoop" % "hadoop-mapreduce-client-common" |
      "org.apache.hadoop" % "hadoop-yarn-api" |
      "org.apache.hadoop" % "hadoop-yarn-client" |
      "org.apache.hadoop" % "hadoop-mapreduce-client-core" |
      "org.apache.hadoop" % "hadoop-yarn-server-common" |
      "org.apache.hadoop" % "hadoop-mapreduce-client-shuffle" |
      "org.apache.hadoop" % "hadoop-mapreduce-client-jobclient" |
      "org.apache.zookeeper" % "zookeeper" |
      "org.slf4j" % "slf4j-log4j12" |
      "log4j" % "log4j" |
      "org.apache.spark" % "spark-core_2.11" |
      "org.apache.spark" % "spark-launcher_2.11" |
      "org.apache.spark" % "spark-network-common_2.11" |
      "org.apache.spark" % "spark-network-shuffle_2.11" |
      "org.apache.spark" % "spark-unsafe_2.11" |
      "org.tachyonproject" % "tachyon-client" |
      "org.tachyonproject" % "tachyon-underfs-hdfs" |
      "org.tachyonproject" % "tachyon-underfs-s3" |
      "org.tachyonproject" % "tachyon-underfs-local" |
      "org.apache.kafka" % "kafka_2.11" |
      "commons-validator" % "commons-validator" |
      "commons-beanutils" % "commons-beanutils" |
      "commons-logging" % "commons-logging" |
      "commons-digester" % "commons-digester" |
      "org.apache.commons" % "commons-compress" |
      "commons-cli" % "commons-cli" |
      "org.apache.commons" % "commons-math" |
      "commons-httpclient" % "commons-httpclient" |
      "commons-codec" % "commons-codec" |
      "commons-net" % "commons-net" |
      "commons-configuration" % "commons-configuration" |
      "commons-collections" % "commons-collections" |
      "commons-beanutils" % "commons-beanutils-core" |
      "org.mortbay.jetty" % "jetty-util" |
      "com.sun.jersey.jersey-test-framework" % "jersey-test-framework-grizzly2" |
      "com.sun.jersey" % "jersey-server" |
      "com.sun.jersey" % "jersey-json" |
      "org.codehaus.jettison" % "jettison" |
      "com.sun.jersey.contribs" % "jersey-guice" |
      "io.netty" % "netty-all" |
      "net.java.dev.jets3t" % "jets3t" |
      "org.apache.curator" % "curator-recipes" |
      "org.apache.curator" % "curator-framework" |
      "org.apache.curator" % "curator-client" |
      "org.eclipse.jetty.orbit" % "javax.servlet" |
      "org.apache.commons" % "commons-math3" |
      "org.roaringbitmap" % "RoaringBitmap" |
      "io.netty" % "netty" |
      "com.sun.jersey" % "jersey-core" |
      "org.apache.ivy" % "ivy" |
      "commons-io" % "commons-io" |
      "javax.websocket" % "javax.websocket-api" |
      "commons-fileupload" % "commons-fileupload" |
      "commons-codec" % "commons-codec" |
      "io.netty" % "netty" |
      "com.twitter" % "chill_2.11" |
      "com.twitter" % "chill-java" |
      "log4j" % "apache-log4j-extras" |
      "com.101tec" % "zkclient" |
      "org.apache.avro" % "avro-mapred" |
      "org.apache.avro" % "avro-ipc" |
      "org.apache.avro" % "avro-ipc" |
      "org.apache.avro" % "avro" |
      "net.razorvine" % "pyrolite" |
      "org.slf4j" % "jul-to-slf4j" |
      "org.slf4j" % "jcl-over-slf4j"|
      "org.scalatest" % "scalatest_2.11" |
      "com.sun.xml.bind" % "jaxb-impl" |
      "javax.xml.bind" % "jaxb-api" |
      "com.fasterxml.jackson.module" % "jackson-module-scala_2.11"
    )
}