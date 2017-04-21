import sbt._

object ArcusysResolvers {
  val public = "arcusys public" at "https://dev-1.arcusys.fi/mvn/repository/public/"
  val mavenCentral = "central" at "http://repo1.maven.org/maven2/"
  val typesafeReleases = "typesafe-releases" at "https://repo.typesafe.com/typesafe/releases/"
  val typesafeSnapshots = "typesafe snapshots" at "https://repo.typesafe.com/typesafe/snapshots/"
  val liferayPublic = "liferay public" at "https://repository.liferay.com/nexus/content/repositories/public//"
}