package features.activityProfile

import com.arcusys.valamis.lrs.test.tincan.{Activities, Contents}
import features.BaseFeatureTests
import org.apache.commons.io.IOUtils
import org.apache.http.HttpStatus
import org.apache.http.client.entity.EntityBuilder
import org.apache.http.client.methods.HttpPut
import org.apache.http.entity.ContentType
import org.apache.http.impl.client.HttpClients

/**
 * Created by Iliya Tryapitsin on 18/02/15.
 */
trait ActivityProfileTestUtils {
  this: BaseFeatureTests =>

  def addTypicalActivityProfile(profileId: String) = {
    val uri = uriBuilder.setPath("/activities/profile")
      .setParameter("activityId", Activities.typical.get.id.get.toString)
      .setParameter("profileId", profileId)
      .build()

    val httpClient = HttpClients.createDefault()
    val inputStream = IOUtils.toInputStream("some content", "UTF-8")

    val entity = EntityBuilder.create()
      .setStream(inputStream)
      .build()

    val httpPut = new HttpPut(uri)
    httpPut.addHeader("X-Experience-API-Version", apiVersion)
    httpPut.addHeader("Authorization", authString)
    httpPut.addHeader("Content-Type", ContentType.APPLICATION_OCTET_STREAM.toString)
    httpPut.setEntity(entity)

    val response = httpClient.execute(httpPut)

    assert(response.getStatusLine.getStatusCode == HttpStatus.SC_NO_CONTENT)
  }

  def addJSONActivityProfile(profileId: String) = {
    val uri = uriBuilder.setPath("/activities/profile")
      .setParameter("activityId", Activities.typical.get.id.get.toString)
      .setParameter("profileId", profileId)
      .build()

    val httpClient = HttpClients.createDefault()
    val entity = EntityBuilder.create()
      .setText(Contents.json)
      .build()

    val httpPut = new HttpPut(uri)
    httpPut.addHeader("X-Experience-API-Version", apiVersion)
    httpPut.addHeader("Authorization", authString)
    httpPut.addHeader("Content-Type", ContentType.APPLICATION_JSON.toString)
    httpPut.setEntity(entity)

    val response = httpClient.execute(httpPut)

    assert(response.getStatusLine.getStatusCode == HttpStatus.SC_NO_CONTENT)
  }
}
