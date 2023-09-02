package no.telenor.fetch.api

import no.telenor.fetch.Foo
import no.telenor.fetch.TestAppTests
import no.telenor.fetch.fetch
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("GET /foo")
class FooTests : TestAppTests() {
	@Test
	fun `Get foo bar baz`() {
		assert(fetch<Foo>("/foo").body!!.bar == "baz")
	}
}
