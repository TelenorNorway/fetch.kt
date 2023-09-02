package no.telenor.fetch

import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity

interface WithTestRestTemplate : WithExchange {
	val testRestTemplate: TestRestTemplate
	override fun <T> restTemplateExchange(
		requestUrl: String,
		method: HttpMethod,
		body: HttpEntity<*>?,
		responseType: Class<T>,
		parameters: Map<String, String?>
	): ResponseEntity<T> = this.testRestTemplate.exchange(
		requestUrl,
		method,
		body,
		responseType,
		parameters
	)
}
