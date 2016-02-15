package com.arcusys.valamis.lrs.jdbc.database.row

/**
 * Created by Iliya Tryapitsin on 28/01/15.
 */
case class ContextActivityRow(contextKey: ContextRow#Type,
                              activityKey: ActivityRow#Type,
                              tpe: ContextActivityType.Type)