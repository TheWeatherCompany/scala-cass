organization := "com.github.thurstonsand"

name := "ScalaCass"

version := "0.6.18"

scalaVersion := "2.11.12"

scalacOptions ++= Seq(
  "-deprecation",
  "-encoding", "UTF-8",
  "-feature",
  "-language:existentials",
  "-language:higherKinds",
  "-language:implicitConversions",
  "-unchecked",
  "-Xfatal-warnings",
  "-Xlint",
  "-Yno-adapted-args",
  "-Ywarn-numeric-widen",
  "-Ywarn-value-discard",
  "-Xfuture",
  "-Ywarn-unused",
  "-Ywarn-unused-import"
)

credentials += Credentials(Path.userHome / ".sbt" / ".credentials")

publishTo := Some("Artifactory Realm" at s"https://repo.artifacts.weather.com/sun-${
  if (isSnapshot.value) s"snapshot-local;build.timestamp=${new java.util.Date().getTime}" else "release-local"
}")

initialize <<=  scalaVersion { sv => CrossVersion.partialVersion(sv) match {
  case _             => sys.props remove "scalac.patmat.analysisBudget"
}}

scalacOptions in (Compile, console) ~= (_ filterNot (_ == "-Ywarn-unused-import"))
scalacOptions in (Test, console) := (scalacOptions in (Compile, console)).value

parallelExecution in Test := false

resolvers ++= Seq(
  Resolver.jcenterRepo,
  "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"
)

addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.1" cross CrossVersion.full)

libraryDependencies ++= Seq(
  "com.google.code.findbugs" % "jsr305" % "3.0.1" % "compile-internal, test-internal", // Intellij does not like "compile-internal, test-internal", use "provided" instead
  "org.slf4j" % "slf4j-api" % "1.7.21" % "compile-internal, test-internal", // Intellij does not like "compile-internal, test-internal", use "provided" instead
  "com.chuusai" %% "shapeless" % "2.3.1",
  "com.google.guava" % "guava" % "19.0",
  "org.scalatest" %% "scalatest" % "3.0.8" % Test,
  "com.whisk" %% "docker-testkit-scalatest" % "0.9.0-M5" % Test,
  "com.datastax.cassandra" % "cassandra-driver-core" % "3.7.2" classifier "shaded" excludeAll ExclusionRule("com.google.guava", "guava"),
  "com.datastax.cassandra" % "cassandra-driver-extras" % "3.7.2" excludeAll (ExclusionRule("com.datastax.cassandra", "cassandra-driver-core"), ExclusionRule("com.google.guava", "guava")),
  "org.cassandraunit" % "cassandra-unit" % "3.0.0.1" % Test,
  "com.github.ichoran" %% "thyme" % "0.1.2-SNAPSHOT" % Test
)

sourceGenerators in Compile += (sourceManaged in Compile).map(Boilerplate.gen).taskValue

import scalariform.formatter.preferences._
import com.typesafe.sbt.SbtScalariform, SbtScalariform.ScalariformKeys

SbtScalariform.scalariformSettings

ScalariformKeys.preferences := ScalariformKeys.preferences.value
  .setPreference(AlignSingleLineCaseStatements, true)
  .setPreference(DoubleIndentClassDeclaration, true)
  .setPreference(DanglingCloseParenthesis, Force)
  .setPreference(SpacesAroundMultiImports, false)

wartremoverWarnings in (Compile, compile) ++= Seq(
  Wart.Any, Wart.Any2StringAdd, Wart.AsInstanceOf,
  Wart.EitherProjectionPartial, Wart.IsInstanceOf, Wart.ListOps,
  Wart.Null, Wart.OptionPartial,
  Wart.Product, Wart.Return, Wart.Serializable,
  Wart.TryPartial, Wart.Var,
  Wart.Enumeration, Wart.FinalCaseClass, Wart.JavaConversions)
wartremoverWarnings in (Compile, console) := Seq.empty

publishMavenStyle := true
pomIncludeRepository := (_ => false)

licenses := Seq("MIT" -> url("http://www.opensource.org/licenses/mit-license.php"))
homepage := Some(url("https://github.com/thurstonsand/scala-cass"))
pomExtra :=
  <scm>
    <url>git@github.com/TheWeatherCompany/scala-cass.git</url>
    <connection>scm:git:git@github.com/TheWeatherCompany/scala-cass.git</connection>
  </scm>
  <developers>
    <developer>
      <id>thurstonsand</id>
      <name>Thurston Sandberg</name>
      <url>https://github.com/thurstonsand</url>
    </developer>
    <developer>
      <id>dnatic09</id>
      <name>Daniel Natic</name>
      <url>https://github.com/dnatic09</url>
    </developer>
  </developers>
