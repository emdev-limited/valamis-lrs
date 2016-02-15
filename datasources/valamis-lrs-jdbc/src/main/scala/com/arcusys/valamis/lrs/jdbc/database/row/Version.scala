package com.arcusys.valamis.lrs.jdbc.database.row

import org.joda.time.DateTime

/**
 * Created by Iliya Tryapitsin on 20/01/15.
 */
case class Version(migrationName: String,
                   appliedDate: DateTime)
