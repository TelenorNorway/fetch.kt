package no.telenor.fetch

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
open class TestAppTests : TestFetch {
	@Autowired
	override lateinit var testRestTemplate: LocalTestRestTemplate
}

