package com.arcusys.valamis.lrs.liferay.test

import java.io.InputStream
import javax.servlet.ServletInputStream

/**
 * Created by Iliya Tryapitsin on 22/01/15.
 */
class DelegatingServletInputStream(val sourceStream: InputStream) extends ServletInputStream {

  def read() = this.sourceStream.read()

  override def close() = {
    this.sourceStream.close()
    super.close()
  }
}