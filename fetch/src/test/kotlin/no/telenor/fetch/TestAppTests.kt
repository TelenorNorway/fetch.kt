package no.telenor.fetch

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.tomakehurst.wiremock.WireMockServer
import no.telenor.fetch.wiremock.WiremockContextInitializer
import org.junit.jupiter.api.AfterEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = [WiremockContextInitializer::class])
open class TestAppTests {
	@Suppress("SpringJavaInjectionPointsAutowiringInspection")
	@Autowired
	lateinit var wm: WireMockServer

	val mapper = ObjectMapper()

	@AfterEach
	fun cleanupAfterTest() = wm.resetAll()
}
