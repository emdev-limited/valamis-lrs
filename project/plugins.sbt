addSbtPlugin("com.earldouglas" % "xsbt-web-plugin" % "1.0.0")

//addSbtPlugin("com.typesafe.sbt" % "sbt-scalariform" % "1.6.0")

addSbtPlugin("com.typesafe.sbt" % "sbt-twirl" % "1.0.4")

addSbtPlugin("net.virtual-void" % "sbt-dependency-graph" % "0.7.5")

addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "0.13.0")

addSbtPlugin("com.typesafe.sbt" % "sbt-osgi" % "0.8.0")

//to resolve Valamis SBT plugins
resolvers ++= Seq(
  Resolver.url("arcusys-public-releases",
    url("https://dev-1.arcusys.fi/mvn/repository/public/"))(Resolver.ivyStylePatterns)
)

addSbtPlugin("com.arcusys.valamis" % "sbt-plugins" % "1.0.1")
