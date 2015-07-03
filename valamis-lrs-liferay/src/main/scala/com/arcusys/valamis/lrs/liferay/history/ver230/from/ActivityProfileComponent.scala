package com.arcusys.valamis.lrs.liferay.history.ver230.from

import scala.slick.jdbc._
import com.arcusys.valamis.lrs.liferay.history.BaseComponent

/**
 * Created by Iliya Tryapitsin on 26/03/15.
 */
trait ActivityProfileComponent { this: BaseComponent =>
  private implicit val getResult = GetResult(r => {
    (
      r.nextLong(),         //`id_` bigint(20) NOT NULL,
      r.nextLong(),         //`documentId` bigint(11) NOT NULL,
      r.nextStringOption(), //`activityId` varchar(512) DEFAULT NULL,
      r.nextStringOption() //`profileId` varchar(512) DEFAULT NULL,
    )
  })

  protected def activityProfiles(implicit session: JdbcBackend#Session) = loadActivityProfiles

  def loadActivityProfiles(implicit session: JdbcBackend#Session) = StaticQuery
    .queryNA[(Long, Long, String, String)](s"select * from ${getTableName("Learn_LFTincanActProfile")}")
    .list
}