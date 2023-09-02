package no.telenor.fetch

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@SpringBootApplication
@RestController
open class TestApp {
	@GetMapping("/foo")
	fun foo() = Foo()
}
