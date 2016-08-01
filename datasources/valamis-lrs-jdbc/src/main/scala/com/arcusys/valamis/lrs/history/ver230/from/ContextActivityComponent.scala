package com.arcusys.valamis.lrs.history.ver230.from

import com.arcusys.valamis.lrs.history.ver230.from.row.ContextActivityRow
import com.arcusys.valamis.lrs.serializer.EnumNameIgnoreCaseSerializer
import com.arcusys.valamis.lrs.tincan.{StatementObjectType, ActivityReference, ContextActivities}
import com.arcusys.json.JsonHelper

import scala.slick.jdbc._
import com.arcusys.valamis.lrs.history.BaseComponent

/**
 * Created by Iliya Tryapitsin on 25/03/15.
 */
trait ContextActivityComponent {
  this: BaseComponent =>
  private implicit val getResult = GetResult(r => {
    ContextActivityRow(
      r.nextLong(), //`id_` bigint(20) NOT NULL,
      r.nextStringOption(), //`parent` varchar(3000) DEFAULT NULL,
      r.nextStringOption(), //`grouping` varchar(3000) DEFAULT NULL,
      r.nextStringOption(), //`category` varchar(3000) DEFAULT NULL,
      r.nextStringOption() //`other` varchar(3000) DEFAULT NULL,
    )
  })

  protected def contextActivities(implicit session: JdbcBackend#Session) = loadContextActivities

  def loadContextActivities(implicit session: JdbcBackend#Session) = StaticQuery
    .queryNA[ContextActivityRow](s"select * from ${getTableName("Learn_LFTincanCtxActivities")}")
    .list

  protected def getContextActivities(key: Option[Long])(implicit session: JdbcBackend#Session): Option[ContextActivities] = key match {
    case None => None
    case Some(v) => {
      contextActivities.find(x => x.id == v) match {
        case None => None
        case Some(contextActivity) => Some(
          ContextActivities(
            parent = getActivityReferences(contextActivity.parent),
            grouping = getActivityReferences(contextActivity.grouping),
            category = getActivityReferences(contextActivity.category),
            other = getActivityReferences(contextActivity.other)
          )
        )
      }

    }
  }

  protected def getActivityReferences(str: Option[String])(implicit session: JdbcBackend#Session) = str match {
    case Some(v) if v.isEmpty => Seq[ActivityReference]()
    case Some(v) if !v.isEmpty => JsonHelper.fromJson[Seq[ActivityReference]](v, new EnumNameIgnoreCaseSerializer(StatementObjectType))
    case None => Seq[ActivityReference]()
  }
}
