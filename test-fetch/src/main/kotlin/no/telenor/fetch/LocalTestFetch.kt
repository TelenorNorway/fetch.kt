package no.telenor.fetch

import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Lazy

// Note: A combination of Lazy-TestConfiguration is required to make this component/service
// available as an autowire dependency.
@Lazy
@TestConfiguration
open class LocalTestFetch(
	private val restTemplateBuilder: RestTemplateBuilder
) : TestFetch {

	@LocalServerPort
	var randomServerPort: Int? = null

	private var innerTestRestTemplate: TestRestTemplate? = null

	override val testRestTemplate: TestRestTemplate
		get() {
			if (this.innerTestRestTemplate == null) {
				this.innerTestRestTemplate =
					TestRestTemplate(restTemplateBuilder.rootUri("http://localhost:$randomServerPort/"))
			}
			return this.innerTestRestTemplate!!
		}

	inline operator fun <reified T> invoke(
		url: String,
		vararg urlArgs: Pair<String, Any?>,
		noinline block: (@Scoped FetchBuilder<WithExchange>).() -> Unit = {},
	) = fetch<T>(url, *urlArgs, block = block)

	inline operator fun <reified T> invoke(
		baseUrl: String,
		url: String,
		vararg urlArgs: Pair<String, Any?>,
		noinline block: (@Scoped FetchBuilder<WithExchange>).() -> Unit = {}
	) = fetch<T>(baseUrl, url, *urlArgs, block = block)

	inline operator fun <reified T> invoke(
		baseUrl: String,
		url: String,
		noinline block: (@Scoped FetchBuilder<WithExchange>).() -> Unit = {}
	) = fetch<T>(baseUrl, url, block)

}
