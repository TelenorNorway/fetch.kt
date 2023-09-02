package no.telenor.fetch

import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.web.client.RestTemplate

interface WithRestTemplate : WithExchange {
	val restTemplate: RestTemplate
	override fun <T> restTemplateExchange(
		requestUrl: String,
		method: HttpMethod,
		body: HttpEntity<*>?,
		responseType: Class<T>,
		parameters: Map<String, String?>
	): ResponseEntity<T> = this.restTemplate.exchange(
		requestUrl,
		method,
		body,
		responseType,
		parameters
	)
}
