package com.arcusys.valamis.lrs.api

/**
 * Created by Iliya Tryapitsin on 25/02/15.
 */
class FailureRequestException(val responseCode: Int, val respMsg: String = "")
  extends Exception(s"Failure request $responseCode: $respMsg")
