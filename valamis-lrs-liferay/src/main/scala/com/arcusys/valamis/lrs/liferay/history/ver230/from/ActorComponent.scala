package com.arcusys.valamis.lrs.liferay.history.ver230.from

  import com.arcusys.valamis.lrs.liferay.history.ver230.from.row.ActorRow
import com.arcusys.valamis.lrs.tincan._
import com.arcusys.valamis.utils.serialization.JsonHelper

import scala.slick.jdbc._
import scala.tools.scalap.scalax.util.StringUtil
import com.arcusys.valamis.lrs.liferay.history.BaseComponent

/**
 * Created by Iliya Tryapitsin on 24/03/15.
 */
trait ActorComponent {
  this: BaseComponent =>
  private implicit val getResult = GetResult(r => {
    ActorRow(
      r.nextLong(), //`id_` bigint(20) NOT NULL,
      r.nextStringOption(), //`tincanID` varchar(512) DEFAULT NULL,
      r.nextStringOption().map { StringUtil.decapitalize }, //`objectType` varchar(512) DEFAULT NULL,
      r.nextStringOption(), //`name` varchar(3000) DEFAULT NULL,
      r.nextStringOption(), //`mbox` varchar(3000) DEFAULT NULL,
      r.nextStringOption(), //`mbox_sha1sum` varchar(3000) DEFAULT NULL,
      r.nextStringOption(), //`openid` varchar(3000) DEFAULT NULL,
      r.nextStringOption(), //`account` longtext,
      r.nextStringOption() //`memberOf` varchar(3000) DEFAULT NULL,
    )
  })

  protected def actors(implicit session: JdbcBackend#Session) = loadActors

  def loadActors (implicit session: JdbcBackend#Session)= StaticQuery
    .queryNA[ActorRow](s"select * from ${getTableName("Learn_LFTincanActor")}")
    .list

  protected def getActor(key: Option[Long])(implicit session: JdbcBackend#Session): Option[Actor] = key.map(getActor)

  protected def getActor(key: Long)(implicit session: JdbcBackend#Session): Actor = {
    val row = actors.find(x => x.key == key)

    require(row.isDefined, s"Cann't find actor for key = $key")
    require(row.get.objectType.isDefined, s"objectType should be defined in Actor with key = $key")

    row.get.objectType match {
      case Some(Constants.Tincan.Agent) => Agent(row.get.name, row.get.mBox, row.get.mBoxSha1Sum, row.get.openId, getAccounts(row.get.account))

      case Some(Constants.Tincan.Group) => Group(row.get.name, None, row.get.mBox, row.get.mBoxSha1Sum, row.get.openId, getAccounts(row.get.account))
    }
  }

  protected def getAgent(key: Long)(implicit session: JdbcBackend#Session): Agent = getActor(Some(key)).asInstanceOf[Agent]

  private def getAccounts(str: Option[String])(implicit session: JdbcBackend#Session) = str match {
    case None => None
    case Some("") => None
    case Some(v) => Some(JsonHelper.fromJson[Account](v))
  }
}