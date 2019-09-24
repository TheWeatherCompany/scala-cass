package com.weather.scalacass

import java.time.{Instant, LocalDate, LocalTime, ZonedDateTime}

import com.datastax.driver.core.{Cluster, DataType}
import com.google.common.reflect.TypeToken
import com.weather.scalacass.CassFormatEncoder.sameTypeCassFormatEncoder
import com.weather.scalacass.CassFormatDecoderVersionSpecific.codecCassFormatDecoder

object Implicits {
  implicit val timeEncoder: CassFormatEncoder[LocalTime] = sameTypeCassFormatEncoder(DataType.time)
  implicit val timeDecoder: CassFormatDecoder[LocalTime] = codecCassFormatDecoder(TypeToken.of(classOf[LocalTime]))

  implicit val dateEncoder: CassFormatEncoder[LocalDate] = sameTypeCassFormatEncoder(DataType.date)
  implicit val dateDecoder: CassFormatDecoder[LocalDate] = codecCassFormatDecoder(TypeToken.of(classOf[LocalDate]))

  implicit val instantEncoder: CassFormatEncoder[Instant] = sameTypeCassFormatEncoder(DataType.timestamp)
  implicit val instantDecoder: CassFormatDecoder[Instant] = codecCassFormatDecoder(TypeToken.of(classOf[Instant]))

  implicit def zonedDateTimeEncoder(implicit cluster: Cluster): CassFormatEncoder[ZonedDateTime] =
    sameTypeCassFormatEncoder(cluster.getMetadata.newTupleType(DataType.timestamp, DataType.varchar))
  implicit val zonedDateTimeDecoder: CassFormatDecoder[ZonedDateTime] = codecCassFormatDecoder(TypeToken.of(classOf[ZonedDateTime]))
}