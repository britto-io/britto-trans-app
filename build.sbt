import com.typesafe.sbt.SbtNativePackager.autoImport._
import com.typesafe.sbt.packager.debian.DebianPlugin.autoImport._
import com.typesafe.sbt.packager.linux.LinuxPlugin.autoImport._
import com.typesafe.sbt.packager.archetypes.ServerLoader
import com.typesafe.sbt.packager.docker._


name := """simple-app"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file("."))
  .enablePlugins(PlayJava)
  .enablePlugins(DockerPlugin)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  javaJdbc,
  cache,
  javaWs
)

///////////////////////////////////
// DEBIAN PACKAGING
///////////////////////////////////

//debian package required setting
maintainer := """Todd Fulton <todd@britto.com>"""

//debian package required setting
packageSummary := "Simple Test Service"

//debian package required setting
packageDescription := """Simple Test Service"""

//debian package reccomends setting
debianPackageRecommends in Debian ++= Seq("oracle-java8-installer")

daemonUser in Linux := "simple_app"

daemonGroup in Linux := "simple_app"

serverLoading in Debian := ServerLoader.Systemd

// "fake" task key for debian package
val packageDeb = taskKey[File]("package-deb")

// file location of .deb file
//packageDeb := (baseDirectory in Compile).value / "target" / (name.value + "_" + version.value + ".deb")

//include conf directory for run
unmanagedClasspath in Runtime += baseDirectory.value.getParentFile.getParentFile  / "conf"

///////////////////////////////////
// DOCKER PACKAGING
///////////////////////////////////
packageName in Docker := name.value
version in Docker := name.value
maintainer in Docker := maintainer.value

dockerCommands := Seq()
dockerCommands := Seq(
	Cmd("FROM", "dockerfile/java:latest"),
	Cmd("MAINTAINER", maintainer.value),
	Cmd("WORKDIR", "/opt/docker"),
	Cmd("ADD", "opt", "/opt"),
	Cmd("RUN", "chown", "-R", "daemon:daemon", "."),
	Cmd("USER", "daemon"),
	Cmd("ENTRYPOINT", "bin/simple-app", "-Dhttp.port=8080"),
	Cmd("CMD", "")
)

