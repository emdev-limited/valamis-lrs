# Valamis LRS

**https://valamis.arcusys.com/**

[![build status](https://api.travis-ci.org/arcusys/valamis-lrs.png)](http://travis-ci.org/arcusys/valamis-lrs)

### NOTE
Since version 3.4, Valamis Community Edition is separated into three packages on GitHub: Valamis LRS (Learning Record Store, this repository),
Learning Paths (https://github.com/arcusys/learning-paths), Valamis components (https://github.com/arcusys/Valamis). You need to compile all of these.

### Building
This is an sbt project.

#### Liferay 6.2
Go to CommonSettings.scala and change the line #14   
`val liferay = Liferay620`

Run

`sbt -J-Xss8M -mem 4096 clean package`

Deploy to the running Liferay instance
`sbt deploy`

#### Liferay DXP
Go to CommonSettings.scala and change the line #14   

`val liferay = Liferay700`

Run

`sbt -J-Xss8M -mem 4096 clean osgiFullPackage`

Deploy the package and all dependencies to the running Liferay instance

`sbt osgiFullDeploy`


### Known issues
If you change CommonSettings.scala, you must run **clean** command!

If you have several tomcat instances running, specify liferay home dir in deploy and osgiFullDeploy commands:

`sbt deploy /opt/liferay-portal-6.2-ce-ga6`

`sbt osgiFullDeploy /opt/liferay-dxp-digital-enterprise-7.0-sp4`

## Version 3.4

## Version 3.2

## Version 3.0.3

## Version 2.6.1

## Version 2.5
  - LRS performance improve
  - Include Sybase driver to Valamis LRS
  - Investigate Apache Spark for embedding into Valamis LRS
  - New API for Valamis
  - Added performance reports. http://localhost:8080/valamis-lrs-portlet/metrics/
  