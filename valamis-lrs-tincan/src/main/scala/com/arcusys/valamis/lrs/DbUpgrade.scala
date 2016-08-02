package com.arcusys.valamis.lrs

trait DbUpgrade {

  // apply migration
  def up(lrs: Lrs): Unit

  // reverse migration (optional)
  def down(lrs: Lrs): Unit
}

