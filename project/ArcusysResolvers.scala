import sbt._

object ArcusysResolvers {
  val public            = "arcusys public"    at "https://nexus.intra.arcusys.fi/mvn/content/repositories/public/"
  val releases          = "arcusys releases"  at "https://nexus.intra.arcusys.fi/mvn/content/repositories/arcusys-releases/"
  val snapshots         = "arcusys snapshots" at "https://nexus.intra.arcusys.fi/mvn/content/repositories/arcusys-snapshots/"
  val mavenCentral      = "central"           at "http://repo1.maven.org/maven2/"
  val typesafeReleases  = "typesafe-releases" at "https://repo.typesafe.com/typesafe/releases/"
  val typesafeSnapshots = "typesafe snapshots"at "https://repo.typesafe.com/typesafe/snapshots/"
}