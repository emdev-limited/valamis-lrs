package com.arcusys.valamis.lrs.history.ver230.from.row

/**
 * Created by Iliya Tryapitsin on 25/03/15.
 */
//`id_` bigint(20) NOT NULL,
//`actorID` int(11) DEFAULT NULL,
//`verbID` varchar(2000) DEFAULT NULL,
//`verbDisplay` longtext,
//`objType` varchar(2000) DEFAULT NULL,
//`objID` int(11) DEFAULT NULL,
case class SubStatementRow(key: Long,
                            actorId: Option[Long],
                            verbId: Option[String],
                            verbDisplay: Option[String],
                            objType: Option[String],
                            objId: Option[Long])
