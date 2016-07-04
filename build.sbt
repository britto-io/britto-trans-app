import com.typesafe.sbt.SbtNativePackager.autoImport._
import com.typesafe.sbt.packager.debian.DebianPlugin.autoImport._
import com.typesafe.sbt.packager.linux.LinuxPlugin.autoImport._
import com.typesafe.sbt.packager.archetypes.ServerLoader
import com.typesafe.sbt.packager.docker._


name := """britto-trans-app"""

version := "1.0-SNAPSHOT"

lazy val persistence = RootProject(uri("https://github.com/britto-io/britto-persistence-lib.git#1.0.0"))

lazy val root = (project in file("."))
  .enablePlugins(PlayJava)
  .enablePlugins(DockerPlugin)
  .dependsOn(persistence)
  .aggregate(persistence)
  .settings(libraryDependencies ++= Seq()
  )

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  javaJdbc,
  cache,
  javaWs
)

unmanagedClasspath in Runtime += baseDirectory.value.getParentFile.getParentFile / "conf"

///////////////////////////////////
// DEBIAN PACKAGING
///////////////////////////////////
packageSummary := "Transaction layer application"
packageDescription := """First pass at the transaction layer application."""
maintainer := """Todd Fulton <todd@britto.com>"""

debianPackageRecommends in Debian ++= Seq("oracle-java8-installer")

daemonUser in Linux := "trans_app"
daemonGroup in Linux := "trans_app"
serverLoading in Debian := ServerLoader.Systemd

// "fake" task key for debian package
val packageDeb = taskKey[File]("package-deb")

// file location of .deb file
//packageDeb := (baseDirectory in Compile).value / "target" / (name.value + "_" + version.value + ".deb")

///////////////////////////////////
// DOCKER PACKAGING
///////////////////////////////////
packageName in Docker := name.value
version in Docker := version.value
maintainer in Docker := maintainer.value

dockerExposedPorts := Seq(8080)
dockerEntrypoint := Seq("bin/britto-trans-app", "-Dhttp.port=8080")

