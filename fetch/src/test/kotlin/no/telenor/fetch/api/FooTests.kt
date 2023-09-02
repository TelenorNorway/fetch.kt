package no.telenor.fetch.api

import no.telenor.fetch.Foo
import no.telenor.fetch.TestAppTests
import no.telenor.fetch.wiremock.stubs.fooOkStub
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpMethod

@DisplayName("GET /foo")
class FooTests : TestAppTests() {
	val testRestTemplate = TestRestTemplate()

	@LocalServerPort
	var port: Int? = null


	@Test
	fun `Get foo bar baz`() {
		wm.stubFor(fooOkStub)
		assert(
			testRestTemplate.exchange(
				"http://localhost:$port/foo",
				HttpMethod.GET,
				null,
				Foo::class.java
			).body!!.bar == "baz"
		)
	}
}
