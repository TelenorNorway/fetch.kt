# Fetch.kt

A higher-level `fetch` function for Spring Boot 3's `RestTemplate`.

> **Warning**
>
> This package is built for Kotlin! You probably can use it in Java,
> however.. No support will be provided for Java consumers.

## Dependency

See gradle plugin [no.ghpkg](https://github.com/testersen/no.ghpkg)
for setup instructions.

```kotlin
plugins {
	id("no.ghpkg") version "0.1.6"
}

repositories {
	git.hub("telenornorway", "fetch.kt")
}

dependencies {
	val fetchVersion = "<version>"
	implementation("no.telenor.fetch:fetch:$fetchVersion")
	testImplementation("no.telenor.fetch:test-fetch:$fetchVersion")
}
```

## Usage

```kotlin
// MyApiClient.kt
@Service
class MyApiClient(
	restTemplate: RestTemplateBuilder,
	internal val configuration: MyApiConfiguration,
) : Fetch {
	override val restTemplate = restBuilder.build()
}

data class HelloWorld(
	val hello: String,
)

// myRequest.kt
fun MyApiClient.myRequest(name: String) = fetch<HelloWorld>(
	configuration.baseUrl,
	"/hello/{name}",
	"name" to name
) {
	get
	Accept header Json
}
```

## Usage in Tests

```kotlin
// MySpringApp.kt
data class Foo(val bar: String = "baz")

@SpringBootApplication
@RestController
class MySpringApp {
	@GetMapping("/foo")
	fun foo() = Foo()
}

fun main(args: Array<String>) {
	runApplication<MySpringApp>(*args)
}
```

```kotlin
// MySpringAppTest.kt
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MySpringAppTest : TestFetch {
	@Autowired
	override lateinit var testRestTemplate: LocalTestRestTemplate
}
```

```kotlin
@DisplayName("GET /foo")
class FooControllerTest : MySpringAppTest() {
	@Test
	fun `foo bar baz test`() {
		assert(fetch<Foo>("/foo").body!!.bar == "baz")
	}
}
```
