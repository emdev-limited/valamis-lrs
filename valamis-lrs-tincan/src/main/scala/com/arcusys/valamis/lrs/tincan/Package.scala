package com.arcusys.valamis.lrs

package object tincan {
  type StringStringMap = Map[String, String]

  type LanguageMap  = StringStringMap
  type ExtensionMap = StringStringMap
  type ProfileId    = String

  object LanguageMap {
    def apply() = Map[String, String]()
  }

  object ExtensionMap {
    def apply() = Map[String, String]()
  }
}
