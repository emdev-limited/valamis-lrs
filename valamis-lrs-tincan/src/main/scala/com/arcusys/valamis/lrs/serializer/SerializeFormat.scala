package com.arcusys.valamis.lrs.serializer

import com.arcusys.valamis.lrs.tincan.FormatType

/**
 * Created by Iliya Tryapitsin on 29/12/14.
 */

case class SerializeFormat(Type: FormatType.Type = FormatType.Exact,
                           Lang: String = "")

