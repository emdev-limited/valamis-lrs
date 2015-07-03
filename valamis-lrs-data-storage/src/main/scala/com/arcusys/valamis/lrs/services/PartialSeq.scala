package com.arcusys.valamis.lrs.services

/**
 * Created by Iliya Tryapitsin on 20.05.15.
 */
case class PartialSeq[T](seq:    Seq[T],
                         isFull: Boolean)