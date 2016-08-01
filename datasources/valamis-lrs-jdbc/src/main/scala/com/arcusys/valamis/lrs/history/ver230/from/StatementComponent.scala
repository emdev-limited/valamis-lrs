package com.arcusys.valamis.lrs.history.ver230.from

import java.beans.Introspector

import com.arcusys.valamis.lrs.history.ver230.from.row.StatementRow

import scala.slick.jdbc.{GetResult, JdbcBackend, StaticQuery}
import com.arcusys.valamis.lrs.history.BaseComponent
import com.arcusys.valamis.lrs.history.Helper._

/**
 * Created by Iliya Tryapitsin on 24/03/15.
 */
trait StatementComponent { this: BaseComponent =>

  private implicit val getResult = GetResult(r => {
    StatementRow(
      r.nextLong(),             //`id_` bigint(20) NOT NULL
      r.nextStringOption(),     //`tincanID` varchar(512) DEFAULT NULL
      r.nextLongOption(),       //`actorID` int(11) DEFAULT NULL
      r.nextStringOption(),     //`verbID` varchar(2000) DEFAULT NULL
      r.nextStringOption(),           //`verbDisplay` longtext
      r.nextStringOption().map {Introspector.decapitalize},     //`objType` varchar(2000) DEFAULT NULL
      r.nextLongOption(),       //`objID` int(11) DEFAULT NULL
      r.nextLongOption(),       //`resultID` int(11) DEFAULT NULL
      r.nextLongOption(),       //`contextID` int(11) DEFAULT NULL
      getDateTimeOption(r.nextDateOption()), //`timestamp` datetime DEFAULT NULL
      getDateTimeOption(r.nextDateOption()), //`stored` datetime DEFAULT NULL
      r.nextLongOption(),       //`authorityID` int(11) DEFAULT NULL
      r.nextStringOption()      //`version` varchar(2000) DEFAULT NULL
    )
  })

  protected def statements(implicit session: JdbcBackend#Session) = loadStatements

  def loadStatements(implicit session: JdbcBackend#Session) = StaticQuery
    .queryNA[StatementRow](s"select * from ${getTableName("Learn_LFTincanLrsStatement")}")
    .list
}
