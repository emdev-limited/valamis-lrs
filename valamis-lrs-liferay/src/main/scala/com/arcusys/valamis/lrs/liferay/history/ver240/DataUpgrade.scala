package com.arcusys.valamis.lrs.liferay.history.ver240

import com.arcusys.valamis.lrs.liferay.history.ver240.from.DataContext
import com.arcusys.valamis.lrs.jdbc.JdbcLrs
import com.arcusys.valamis.lrs.liferay.history.BaseUpgrade

class DataUpgrade(val lrs: JdbcLrs) extends  BaseUpgrade{
  val dataContext = new DataContext

  def upgrade = lrs.db.withSession { implicit session =>{
    tryAction(dataContext.encodeResults())
  }}
}

