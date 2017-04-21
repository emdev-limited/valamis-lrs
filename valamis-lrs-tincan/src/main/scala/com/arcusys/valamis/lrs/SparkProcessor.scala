package com.arcusys.valamis.lrs

import com.arcusys.valamis.lrs.tincan.Statement
import com.arcusys.valamis.lrs.utils.PartialSeq

/**
 * Spark Processor
 */
trait SparkProcessor {
  val support: Boolean

  def init: Unit

  def findStatementsByParams(params: StatementQuery): Seq[Statement]
}
