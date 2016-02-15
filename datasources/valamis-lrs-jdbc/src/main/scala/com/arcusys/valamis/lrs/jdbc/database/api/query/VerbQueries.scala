package com.arcusys.valamis.lrs.jdbc.database.api.query

import com.arcusys.valamis.lrs.jdbc.database.LrsDataContext

/**
 * Created by Iliya Tryapitsin on 09.07.15.
 */
trait VerbQueries  extends TypeAliases {
  this: LrsDataContext =>

  import executionContext.driver.simple._

  private type FilterCol = ConstColumn[Option[String]]
  private type NumCol    = ConstColumn[Long]

  /**
   * Select Verb Id, Verb Display, Activity Id and Activity Name
   * by filter for Verb Display or Activity Name
   * @param filter Filter
   * @return
   */
  def findVerbsActivitiesQ (filter: FilterCol) =
    statements leftJoin activities on {
      (x1, x2) => x1.objectKey === x2.key
    } filter {
      case (s, o) => (s.verbDisplay like filter) || (o.name like filter)
    } map {
      case ((s, o)) => (s.verbId, s.verbDisplay, o.id, o.name)
    } groupBy {
      case s => s
    } map {
      case (k, v) => k
    }

  /**
   * Select Verb Id, Verb Display, Activity Id and Activity Name
   * by filter for Verb Display or Activity Name and then sorted by Verb Id
   * @param filter Filter
   * @return
   */
  def findVerbsActivitiesSortQ (filter: FilterCol) =
    findVerbsActivitiesQ (filter) sortBy {
      case ((verbId, _, _, _)) => verbId
    }

  /**
   * Compiled query for select Verb Id, Verb Display, Activity Id and Activity Name
   * by filter for Verb Display or Activity Name
   */
  val findVerbsActivitiesQC = Compiled(
    (filter: FilterCol,
     limit:  NumCol,
     offset: NumCol) => findVerbsActivitiesQ(filter) drop offset take limit)

  /**
   * Compiled query for select Verb Id, Verb Display, Activity Id and Activity Name
   * by filter for Verb Display or Activity Name and then sorted by Verb Id
   */
  val findVerbsActivitiesSortQC = Compiled(
    (filter: FilterCol,
     limit:  NumCol,
     offset: NumCol) => findVerbsActivitiesSortQ(filter) drop offset take limit)

  /**
   * Compiled query for count items in filter for Verb Display or Activity Name
   */
  val findVerbsActivitiesCountQC = Compiled(
    (filter: FilterCol) => findVerbsActivitiesQ(filter) length)


}
