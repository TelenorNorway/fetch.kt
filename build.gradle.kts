plugins {
	kotlin("jvm") version "1.9.10"
	id("io.spring.dependency-management") version "1.1.3"
	`maven-publish`
	id("no.ghpkg") version "0.1.6"
}

group = "no.telenor.fetch"
version = System.getenv("VERSION") ?: "UNVERSIONED"

repositories {
	mavenCentral()
}

dependencies {
	// note: Snakeyaml is used for project configurations.
	@Suppress("VulnerableLibrariesLocal", "RedundantSuppression")
	implementation("org.springframework.boot:spring-boot-starter-web:3.1.3")
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
