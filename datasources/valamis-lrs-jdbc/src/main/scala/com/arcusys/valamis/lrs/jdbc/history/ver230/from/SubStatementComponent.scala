package com.arcusys.valamis.lrs.jdbc.history.ver230.from

import java.beans.Introspector

import com.arcusys.valamis.lrs.jdbc.history.ver230.from.row.SubStatementRow

import scala.slick.jdbc._
import com.arcusys.valamis.lrs.jdbc.history.BaseComponent

/**
  * Created by Iliya Tryapitsin on 25/03/15.
  */
trait SubStatementComponent { this: BaseComponent =>
   private implicit val getResult = GetResult(r => {
     SubStatementRow(
       r.nextLong(),          //`id_` bigint(20) NOT NULL,
       r.nextLongOption(),    //`actorID` int(11) DEFAULT NULL,
       r.nextStringOption(),  //`verbID` varchar(2000) DEFAULT NULL,
       r.nextStringOption(),        //`verbDisplay` longtext,
       r.nextStringOption().map {Introspector.decapitalize},  //`objType` varchar(2000) DEFAULT NULL,
       r.nextLongOption()     //`objID` int(11) DEFAULT NULL,
     )
   })

  protected def subStatements(implicit session: JdbcBackend#Session) = loadSubStatements

   def loadSubStatements(implicit session: JdbcBackend#Session) = StaticQuery
     .queryNA[SubStatementRow](s"select * from ${getTableName("Learn_LFTCLrsSubStmnt")}")
     .list
 }
