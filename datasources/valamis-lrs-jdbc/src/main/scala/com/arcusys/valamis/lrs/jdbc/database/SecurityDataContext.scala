package com.arcusys.valamis.lrs.jdbc.database

import com.arcusys.valamis.lrs.jdbc.database.converter.{ToTincanConverter, ToRowConverter}
import com.arcusys.valamis.lrs.jdbc.database.schema.{TokenSchema, ApplicationSchema}

trait SecurityDataContext
  extends BaseDataContext
  with ApplicationSchema
  with TokenSchema
  with ToRowConverter
  with ToTincanConverter