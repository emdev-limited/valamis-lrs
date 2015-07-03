package com.arcusys.valamis.lrs.liferay.history.ver230.from.row

/**
 * Created by Iliya Tryapitsin on 18/03/15.
 */

//`id_` bigint(20) NOT NULL,
//`documentId` int(11) DEFAULT NULL,
//`activityId` varchar(512) DEFAULT NULL,
//`profileId` varchar(512) DEFAULT NULL,
case class ActivityProfileRow(id: Long,
                              documentId: Option[Long],
                              activityId: Option[String],
                              profileId: Option[String])
