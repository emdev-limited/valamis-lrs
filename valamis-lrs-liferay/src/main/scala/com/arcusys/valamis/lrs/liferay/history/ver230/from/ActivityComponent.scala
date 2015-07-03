package com.arcusys.valamis.lrs.liferay.history.ver230.from

import com.arcusys.valamis.lrs.liferay.history.ver230.from.row.ActivityRow

import scala.slick.jdbc._
import scala.tools.scalap.scalax.util.StringUtil
import com.arcusys.valamis.lrs.liferay.history.BaseComponent

/**
 * Created by Iliya Tryapitsin on 24/03/15.
 */
trait ActivityComponent { this: BaseComponent =>
  private implicit val getResult = GetResult(r => {
    ActivityRow(
      r.nextLong(),         //`id_` bigint(20) NOT NULL,
      r.nextStringOption(), //`tincanID` varchar(2000) DEFAULT NULL,
      r.nextLongOption(),
      r.nextStringOption().map {StringUtil.decapitalize}, //`objectType` varchar(512) DEFAULT NULL,
      r.nextStringOption(),       //`name` longtext,
      r.nextStringOption(),       //`description` longtext,
      r.nextStringOption(),       //`theType` longtext,
      r.nextStringOption(),       //`moreInfo` longtext,
      r.nextStringOption(),       //`interactionType` longtext,
      r.nextStringOption(),       //`correctResponsesPattern` longtext,
      r.nextStringOption(),       //`choices` longtext,
      r.nextStringOption(),       //`scale` longtext,
      r.nextStringOption(),       //`source` longtext,
      r.nextStringOption(),       //`target` longtext,
      r.nextStringOption(),       //`steps` longtext,
      r.nextStringOption()        //`extensions` longtext,
    )
  })

  protected def activities(implicit session: JdbcBackend#Session) = loadActivities

  def loadActivities(implicit session: JdbcBackend#Session) = StaticQuery
    .queryNA[ActivityRow](s"select * from ${getTableName("Learn_LFTincanActivity")}")
    .list
}
