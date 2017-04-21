package com.arcusys.valamis.lrs.jdbc.history.ver240

import com.arcusys.valamis.lrs.jdbc.history.BaseUpgrade
import com.arcusys.valamis.lrs.jdbc.history.ver240.from.DataContext
import com.arcusys.valamis.lrs.jdbc.JdbcLrs

class DataUpgrade(val lrs: JdbcLrs) extends  BaseUpgrade{
  val dataContext = new DataContext

  def upgrade = lrs.db.withSession { implicit session =>
    tryAction(dataContext.encodeResults())
  }
}

