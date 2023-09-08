package no.telenor.fetch

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.WebApplicationType
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestTemplate

@SpringBootApplication
@RestController
open class TestApp(
	restTemplateBuilder: RestTemplateBuilder,
	@Value("\${externalServerPort}")
	private val externalServerPort: Int
) : Fetch {
	override val restTemplate: RestTemplate = restTemplateBuilder.build()

	@GetMapping("/foo")
	fun foo() = fetch<Foo>("http://localhost:$externalServerPort/foo").body!!
}

fun main(args: Array<String>) {
	val app = runApplication<TestApp>(*args) {
		webApplicationType = WebApplicationType.NONE
	}
}
