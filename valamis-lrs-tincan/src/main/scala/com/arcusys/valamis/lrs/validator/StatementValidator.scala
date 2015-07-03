package com.arcusys.valamis.lrs.validator

import com.arcusys.valamis.lrs.tincan.Constants.Tincan
import com.arcusys.valamis.lrs.tincan.Constants.Tincan.Field._
import com.arcusys.valamis.lrs.tincan.Constants.Tincan._
import com.arcusys.valamis.lrs.tincan._
import com.arcusys.valamis.utils.serialization.JSONDeserializerException
import org.json4s.JsonAST.JValue

object StatementValidator {

  def checkNotNull(jValue: JValue) = {

    jValue \ id           notNull

    jValue \ Actor        notNull

    jValue \ Tincan.Verb         notNull

    jValue \ `object`     notNull

    jValue \ Tincan.Result       notNull

    jValue \ context      notNull

    jValue \ timestamp    notNull

    jValue \ stored       notNull

    jValue \ authority    notNull

    jValue \ version      notNull

    jValue \ Attachments  notNull
  }

  def checkRequirements(v: Statement) = {
    if(v.verb.isVoided && !v.obj.isInstanceOf[StatementReference] )
      throw new JSONDeserializerException("statement verb voided does not use object \"StatementRef\"")

    v.attachments.foreach { attachment =>
      LanguageMapValidator checkRequirements attachment.display
      LanguageMapValidator checkRequirements attachment.description
    }

    v
  }

}
