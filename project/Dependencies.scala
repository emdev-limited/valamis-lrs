import sbt._

object Version {
  val scala                = "2.10.5"
  val slick                = "2.1.0"
  val slickDrivers         = "2.1.0"
  val slickJodaMapper      = "1.2.0"
  val config               = "1.2.1"
  val json4s               = "3.2.11"
  val scalatest            = "2.2.3"
  val scalacheck           = "1.12.1"
  val slf4j                = "1.6.4"
  val httpClient           = "4.4"
  val httpMime             = "4.4"
  val portlet              = "2.0"
  val liferayPortal        = "6.2.2"
  val akka                 = "2.3.9"
  val websocket            = "1.1"

  val jodaConvert          = "1.7"
  val jodaTime             = "2.3"
  val scalaGuice           = "3.0.2"
  val guice                = "3.0"
  val mockito              = "1.10.17"
  val junit                = "4.12"
  val logback              = "1.1.3"
  val servletApi           = "3.0.1"
  val commonsValidator     = "1.4.1"
  val commonsLang          = "2.6"
  val oauth                = "20100527"
  val fileUpload           = "1.3.1"
  val valamis              = "2.4"
  val slickMigrations      = "2.1.0"
  val commonsIO            = "1.3.2"

  // Db
  val h2                   = "1.3.170"
  val hsql                 = "0.0.15"
  val mysql                = "5.1.34"
  val postgres             = "9.4-1201-jdbc41"
}

object Libraries {
  val akkaActor             = "com.typesafe.akka"             %%  "akka-actor"                            % Version.akka
  val slick                 = "com.typesafe.slick"            %%  "slick"                                 % Version.slick
  val slickJodaMapper       = "com.github.tototoshi"          %%  "slick-joda-mapper"                     % Version.slickJodaMapper
  val slickDrivers          = "com.arcusys.slick"             %%  "slick-drivers"                         % Version.slickDrivers
  val json4s                = "org.json4s"                    %%  "json4s-native"                         % Version.json4s
  val json4sJackson         = "org.json4s"                    %%  "json4s-jackson"                        % Version.json4s
  val json4sExt             = "org.json4s"                    %%  "json4s-ext"                            % Version.json4s
  val scalatest             = "org.scalatest"                 %%  "scalatest"                             % Version.scalatest
  val scalacheck            = "org.scalacheck"                %%  "scalacheck"                            % Version.scalacheck
  val scalaGuice            = "net.codingwell"                %%  "scala-guice"                           % Version.scalaGuice
  val config                = "com.typesafe"                  %   "config"                                % Version.config
  val guiceMultibindings    = "com.google.inject.extensions"  %   "guice-multibindings"                   % Version.guice
  val commonsIO             = "org.apache.commons"            %   "commons-io"                            % Version.commonsIO
  val slf4j                 = "org.slf4j"                     %   "slf4j-api"                             % Version.slf4j
  val logback               = "ch.qos.logback"                %   "logback-classic"                       % Version.logback
  val jodaTime              = "joda-time"                     %   "joda-time"                             % Version.jodaTime
  val jodaConvert           = "org.joda"                      %   "joda-convert"                          % Version.jodaConvert
  val commonsValidator      = "commons-validator"             %   "commons-validator"                     % Version.commonsValidator
  val commonsLang           = "commons-lang"                  %   "commons-lang"                          % Version.commonsLang

  // Web
  val httpClient            = "org.apache.httpcomponents"     %   "httpclient"                            % Version.httpClient
  val httpMime              = "org.apache.httpcomponents"     %   "httpmime"                              % Version.httpMime
  val guiceServlet          = "com.google.inject.extensions"  %   "guice-servlet"                         % Version.guice
  val javaxServlet          = "javax.servlet"                 %   "javax.servlet-api"                     % Version.servletApi
  val fileUpload            = "commons-fileupload"            %   "commons-fileupload"                    % Version.fileUpload
  val mockito               = "org.mockito"                   %   "mockito-all"                           % Version.mockito
  val portlet               = "javax.portlet"                 %   "portlet-api"                           % Version.portlet
  val liferayPortal         = "com.liferay.portal"            %   "portal-service"                        % Version.liferayPortal
  val liferayPortalImpl     = "com.liferay.portal"            %   "portal-impl"                           % Version.liferayPortal
  val websocket             = "javax.websocket"               %   "javax.websocket-api"                   % Version.websocket

  //OAuth 1.0 Provider & Consumer Library
  val oauthCore             = "net.oauth.core"                %   "oauth"                                 % Version.oauth
  val oauthConsumer         = "net.oauth.core"                %   "oauth-consumer"                        % Version.oauth
  val oauthProvider         = "net.oauth.core"                %   "oauth-provider"                        % Version.oauth

  // Db
  val h2                    = "com.h2database"                %   "h2"                                    % Version.h2
  val hsql                  = "com.danidemi.jlubricant"       %   "jlubricant-embeddable-hsql"            % Version.hsql
  val mysql                 = "mysql"                         %   "mysql-connector-java"                  % Version.mysql
  val postgres              = "org.postgresql"                %   "postgresql"                            % Version.postgres

  // Valamis
  val valamisUtils          = "com.arcusys.valamis"           %%  "valamis-utils"                         % Version.valamis
  val slickMigration        = "com.arcusys.slick"             %%  "slick-migration"                       % Version.slick
}


object Dependencies {
  import Libraries._

  val testSet = Seq(
    mockito    % Test,
    scalatest  % Test,
    scalacheck % Test
  )

  val testWeb = Seq(
    httpClient % Test,
    httpMime   % Test
  )

  val testDbSet = Seq(
    config   % Test,
    h2       % Test,
    hsql     % Test,
    mysql    % Test,
    postgres % Test
  ) ++ testSet

  val jsonSet = Seq(
    json4s,
    json4sExt,
    json4sJackson
  )

  val slickSet = Seq(
    slick,
    slickMigration,
    slickDrivers,
    slickJodaMapper
  )

  val joda = Seq(
    jodaTime,
    jodaConvert
  )

  val common = Seq(
    guiceMultibindings,
    valamisUtils,
    logback,
    slf4j,
    scalaGuice
  ) ++ testSet ++ joda

  val baseWeb = Seq(
    fileUpload,
    guiceServlet,
    javaxServlet % Provided
  )

  val tincan = Seq(
    commonsValidator,
    commonsLang
  ) ++ jsonSet ++ testDbSet

  val database = Seq(
    commonsValidator,
    akkaActor
  ) ++ slickSet ++ jsonSet ++ testDbSet

  val web = Seq(
    commonsIO,
    websocket,
    portlet           % Provided,
    liferayPortal     % Provided,
    liferayPortalImpl % Provided,
    oauthCore,
    oauthConsumer,
    oauthProvider,
    akkaActor
  ) ++ baseWeb ++  jsonSet ++ testDbSet ++ testWeb

  val util = Seq(akkaActor)

  val api = baseWeb ++ testWeb ++ Seq(httpMime, httpClient)
}