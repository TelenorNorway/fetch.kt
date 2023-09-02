plugins {
	kotlin("jvm") version "1.9.10"
	id("io.spring.dependency-management") version "1.1.3"
	`maven-publish`
	id("no.ghpkg") version "0.1.6"
}

repositories {
	mavenCentral()
}

allprojects {
	apply(plugin = "org.jetbrains.kotlin.jvm")
	apply(plugin = "io.spring.dependency-management")
	apply(plugin = "maven-publish")
	apply(plugin = "no.ghpkg")

	group = "no.telenor.fetch"
	version = System.getenv("VERSION") ?: "UNVERSIONED"

	repositories {
		mavenCentral()
	}

	kotlin.jvmToolchain(17)

	publishing {
		repositories {
			mavenLocal()
			github.actions()
		}
		publications.register<MavenPublication>("gpr") {
			from(components["kotlin"])
		}
	}
}
