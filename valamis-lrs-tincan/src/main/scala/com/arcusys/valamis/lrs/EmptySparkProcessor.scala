package com.arcusys.valamis.lrs

import com.arcusys.valamis.lrs.tincan.Statement
import com.arcusys.valamis.lrs.utils.PartialSeq

class EmptySparkProcessor()
  extends SparkProcessor {

  override val support = false

  override def findStatementsByParams(params: StatementQuery): Seq[Statement] = ???

  override def init: Unit = {}
}
