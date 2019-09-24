package com.weather.scalacass

object ScalaCassUnitTestsVersionSpecific {
  type BadTypeException = com.datastax.driver.core.exceptions.CodecNotFoundException
  val extraHeaders = List("ts timestamp", "dt date", "t time", "specialdt tuple<timestamp,varchar>")
}

trait ScalaCassUnitTestsVersionSpecific { this: ScalaCassUnitTests =>
  "date (datastax date)" should "be extracted correctly" in testType[com.datastax.driver.core.LocalDate, Int]("dt", com.datastax.driver.core.LocalDate.fromDaysSinceEpoch(1000), com.datastax.driver.core.LocalDate.fromDaysSinceEpoch(10000))
  "time (long)" should "be extracted correctly" in testType[Time, Int]("t", Time(12345L), Time(54321L))
  "timestamp (java util date)" should "be extracted correctly" in testType[java.util.Date, Int]("ts", new java.util.Date(56565L), new java.util.Date(65656L))
}

class Jdk8ScalaCassUnitTests extends ScalaCassUnitTests {
  import Implicits._
  override def beforeAll(): Unit = {
    super.beforeAll()
    com.weather.scalacass.jdk8.register(client.cluster)
  }

  "date (jdk8 date)" should "be extracted correctly" in testType[java.time.LocalDate, Int]("dt", java.time.LocalDate.now, java.time.LocalDate.now.plusDays(1))
  "time (jdk8 time)" should "be extracted correctly" in testType[java.time.LocalTime, Int]("t", java.time.LocalTime.NOON, java.time.LocalTime.MIDNIGHT)
  "timestamp (jdk8 instant)" should "be extracted correctly" in testType[java.time.Instant, Int]("ts", java.time.Instant.now, java.time.Instant.now.plusSeconds(56L))
  "datetime (jdk8 datetime)" should "be extracted correctly" in testType[java.time.ZonedDateTime, Int]("specialdt", java.time.ZonedDateTime.now, java.time.ZonedDateTime.now.plusHours(4))
}