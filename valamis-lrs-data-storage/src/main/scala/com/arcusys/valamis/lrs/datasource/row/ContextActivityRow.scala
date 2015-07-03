package com.arcusys.valamis.lrs.datasource.row

/**
 * Created by Iliya Tryapitsin on 28/01/15.
 */
case class ContextActivityRow(contextKey: ContextRow#Type,
                              activityKey: ActivityRow#Type,
                              contextActivityType: ContextActivityType.Type)