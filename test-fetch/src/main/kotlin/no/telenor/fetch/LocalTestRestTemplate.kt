package no.telenor.fetch

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Lazy

@Lazy
@TestConfiguration
open class LocalTestRestTemplate(
	@Value("\${local.server.port}") randomServerPort: Int,
	restTemplateBuilder: RestTemplateBuilder
) : TestRestTemplate(restTemplateBuilder.rootUri("http://localhost:$randomServerPort"))
