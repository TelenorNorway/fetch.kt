package no.telenor.fetch

import org.springframework.web.client.RestTemplate

interface WithRestTemplate {
	val restTemplate: RestTemplate
}
