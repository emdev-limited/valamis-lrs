package com.arcusys.valamis.lrs.jdbc.database.typemap

/**
 * Created by Iliya Tryapitsin on 04/01/15.
 */

import java.net.URI

import com.arcusys.json.JsonHelper
import com.arcusys.util.Version
import com.arcusys.valamis.lrs.jdbc.database.BaseDataContext
import com.arcusys.valamis.lrs.jdbc.database.row.ContextActivityType
import com.arcusys.valamis.lrs.security.AuthenticationType
import com.arcusys.valamis.lrs.tincan.{AuthorizationScope, _}
import org.joda.time.Duration

/**
 * Custom Type mappers for Slick.
 */
trait TypeMapper {
  this: BaseDataContext =>

  import driver.simple._

  implicit val localDateMapper: BaseColumnType[URI] = MappedColumnType.base[URI, String](
    to   => to.toString,
    from => new URI(from)
  )

  /** Type mapper for [[org.joda.time.Duration]] */
  implicit val durationTypeMapper: BaseColumnType[Duration] = MappedColumnType.base[Duration, Long](
    d => d.getMillis,
    l => Duration.millis(l)
  )

  implicit val interactionTypeMapper = MappedColumnType.base[InteractionType.Type, String](
    v => v.toString,
    s => InteractionType.withName(s)
  )

  implicit val tincanVersionTypeMapper = MappedColumnType.base[TincanVersion.Type, String](
    v => v.toString,
    s => TincanVersion.withName(s)
  )

  implicit val contextActivityTypeMapper = MappedColumnType.base[ContextActivityType.Type, String](
    v => v.toString,
    s => ContextActivityType.withName(s)
  )

  implicit val contentTypeMapper = MappedColumnType.base[ContentType.Type, String](
    v => v.toString,
    s => ContentType.withName(s)
  )

  implicit val seqMapper = MappedColumnType.base[Seq[String], String](
    seq => JsonHelper.toJson(seq),
    str => JsonHelper.fromJson[Seq[String]](str)
  )

  implicit val seqInteractionComponentMapper = MappedColumnType.base[Seq[InteractionComponent], String](
    seq => JsonHelper.toJson(seq),
    str => if (str.isEmpty)
      Seq[InteractionComponent]()
    else
      JsonHelper.fromJson[Seq[InteractionComponent]](str)
  )

  implicit val versionStringMapping = MappedColumnType.base[Version, String](
    ver => ver.toString,
    str => Version(str)
  )

  /** Type mapper for [[AuthorizationScope.Type]] */
  implicit val scopeIntMapper: BaseColumnType[AuthorizationScope.ValueSet] = MappedColumnType.base[AuthorizationScope.ValueSet, Long](
    values => values.toBitMask.sum,
    bits   => AuthorizationScope.ValueSet.fromBitMask(Array(bits))
  )

  /** Type mapper for [[AuthenticationType.Type]] */
  implicit val authTypeStringMapper: BaseColumnType[AuthenticationType.Type] = MappedColumnType.base[AuthenticationType.Type, String](
    scope => scope.toString,
    str   => AuthenticationType.withName(str)
  )
}



