package features

import javax.xml.bind.DatatypeConverter

import com.arcusys.valamis.lrs.api.{ LrsSettings, LrsAuthBasicSettings}
import org.apache.http.client.utils.URIBuilder

/**
 * Created by Iliya Tryapitsin on 13/02/15.
 */
trait BaseFeatureTests {
  val apiVersion = "1.0.0"
  val login = "test"
  val pass = "test"
  val authString = s"Basic ${DatatypeConverter.printBase64Binary(s"$login:$pass".getBytes)}"

  val uriBuilder = new URIBuilder()
    .setScheme("http")
    .setHost("localhost")
    .setPort(8090)

  implicit val lrs = new LrsSettings(
    address = "http://localhost:8090",
    version = "1.0.0",
    auth = new LrsAuthBasicSettings(
      login = "test",
      password = "test"
    )
  )
}
