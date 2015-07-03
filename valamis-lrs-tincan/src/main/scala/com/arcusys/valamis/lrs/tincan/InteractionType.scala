package com.arcusys.valamis.lrs.tincan

/**
 * Created by Iliya Tryapitsin on 30/12/14.
 */
object InteractionType extends Enumeration {
  type Type = Value

  val Other       = Value(Constants.InteractionType.Other      )
  val Choice      = Value(Constants.InteractionType.Choice     )
  val Likert      = Value(Constants.InteractionType.Likert     )
  val FillIn      = Value(Constants.InteractionType.FillIn     )
  val Numeric     = Value(Constants.InteractionType.Numeric    )
  val Matching    = Value(Constants.InteractionType.Matching   )
  val TrueFalse   = Value(Constants.InteractionType.TrueFalse  )
  val Sequencing  = Value(Constants.InteractionType.Sequencing )
  val LongFillIn  = Value(Constants.InteractionType.LongFillIn )
  val Performance = Value(Constants.InteractionType.Performance)
}
