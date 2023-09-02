dependencies {
	api(project(":common"))
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	@Suppress("VulnerableLibrariesLocal", "RedundantSuppression")
	testImplementation("org.wiremock:wiremock:3.0.0")
}
