# Fetch.kt

A higher-level `fetch` function for Spring Boot 3's `RestTemplate`.

> **Warning**
>
> This module might work for Java, but there is no guarantee and this
> module is not built to run on Java. No support will be provided for
> Java consumers.

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
	implementation("no.telenor.fetch:fetch:<version>")
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
