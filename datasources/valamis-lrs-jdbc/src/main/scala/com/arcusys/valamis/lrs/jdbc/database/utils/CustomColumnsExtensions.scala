package com.arcusys.valamis.lrs.jdbc.database.utils

import com.arcusys.valamis.lrs.tincan.LanguageMap

import scala.slick.ast.Library
import scala.slick.lifted.{FunctionSymbolExtensionMethods, Column, ExtensionMethods}
import scala.language.{implicitConversions, higherKinds}
import FunctionSymbolExtensionMethods._

trait CustomColumnsExtensions {

  implicit def languageMapColumnExtensionMethods(l: Column[LanguageMap]) = new LanguageMapLowerGenColumnExtension[LanguageMap](l)
  implicit def languageMapOptionColumnExtensionMethods(l: Column[Option[LanguageMap]]) = new LanguageMapLowerGenColumnExtension[Option[LanguageMap]](l)

  final class LanguageMapLowerGenColumnExtension[P1](val c: Column[P1]) extends ExtensionMethods[LanguageMap, P1] {
    def lower = Library.LCase.column[P1](n)
  }

}