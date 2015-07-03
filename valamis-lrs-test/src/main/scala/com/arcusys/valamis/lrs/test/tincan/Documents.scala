package com.arcusys.valamis.lrs.test.tincan

import java.util.UUID

/**
 * Created by Iliya Tryapitsin on 01/04/15.
 */

case class Document(id: UUID = UUID.randomUUID(),
                    contents: String)

object Documents {

}
