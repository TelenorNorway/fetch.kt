package no.telenor.fetch

import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity

interface WithExchange {
	fun <T> restTemplateExchange(
		requestUrl: String,
		method: HttpMethod,
		body: HttpEntity<*>?,
		responseType: Class<T>,
		parameters: Map<String, String?>
	): ResponseEntity<T>
}
