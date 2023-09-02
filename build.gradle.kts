plugins {
	kotlin("jvm") version "1.9.10"
	id("org.springframework.boot") version "3.1.3"
	id("io.spring.dependency-management") version "1.1.3"
	`maven-publish`
	id("no.ghpkg") version "0.1.6"
}

repositories {
	mavenCentral()
}

subprojects {
	apply(plugin = "org.jetbrains.kotlin.jvm")
	apply(plugin = "org.springframework.boot")
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

	tasks.withType<Test> {
		useJUnitPlatform()
	}
}
