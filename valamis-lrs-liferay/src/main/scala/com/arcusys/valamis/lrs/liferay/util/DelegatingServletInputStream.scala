package com.arcusys.valamis.lrs.liferay.util

import java.io.InputStream
import javax.servlet.ServletInputStream

class DelegatingServletInputStream(val sourceStream: InputStream) extends ServletInputStream {

  def read() = this.sourceStream.read()

  override def close() = {
    this.sourceStream.close()
    super.close()
  }
}