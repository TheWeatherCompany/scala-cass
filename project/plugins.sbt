resolvers ++= Seq(
  Resolver.sonatypeRepo("releases")
)

addSbtPlugin("org.scalariform" % "sbt-scalariform" % "1.6.0")

addSbtPlugin("org.brianmckenna" % "sbt-wartremover" % "0.14")