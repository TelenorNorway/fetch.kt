@file:Suppress("unused")

package no.telenor.fetch

import com.fasterxml.jackson.databind.ObjectMapper
import no.telenor.fetch.util.*
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import kotlin.reflect.KFunction

private val mapper = ObjectMapper()

class FetchBuilder<Self : Fetch>(val self: Self) {

	// region QueryBuilder
	internal val queryParamMap = mutableMapOf<String, String>()
	internal val urlParametersAppend = mutableMapOf<String, String>()

	infix fun String.queryTo(to: Any?) {
		if (to == null) return
		val key = "q${this.substring(0, 1).uppercase()}${this.substring(1)}"
		queryParamMap[this] = key
		urlParametersAppend[key] = to.toString()
	}

	// endregion

	// region HttpMethod
	@Suppress("MemberVisibilityCanBePrivate")
	var method: HttpMethod = HttpMethod.GET

	// @formatter:off
	val get: Unit get() { this.method = HttpMethod.GET }
	val head: Unit get() { this.method = HttpMethod.HEAD }
	val post: Unit get() { this.method = HttpMethod.POST }
	val put: Unit get() { this.method = HttpMethod.PUT }
	val patch: Unit get() { this.method = HttpMethod.PATCH }
	val delete: Unit get() { this.method = HttpMethod.DELETE }
	val options: Unit get() { this.method = HttpMethod.OPTIONS }
	val trace: Unit get() { this.method = HttpMethod.TRACE }
	// @formatter:on
	// endregion

	// region Headers
	@Suppress("MemberVisibilityCanBePrivate")
	var headers: HttpHeaders = HttpHeaders()

	@Suppress("MemberVisibilityCanBePrivate")
	fun headers(block: (@Scoped HttpHeaders).() -> Unit) {
		this.headers.apply(block)
	}

	@Suppress("MemberVisibilityCanBePrivate")
	infix fun <T> HeaderKey<T>.header(value: T) {
		this.applyHeader(headers, value)
	}

	infix fun String.header(value: String) {
		headers[this] = value
	}

	infix fun String.header(value: HeaderValueWithString<*>) {
		headers[this] = value.stringValue
	}

	infix fun <T> HeaderKey<T>.header(value: HeaderValue<T>) {
		this.header(value.value)
	}

	infix fun HeaderKey<*>.header(value: String) {
		headers[this.name] = value
	}

	infix fun <T> HeaderKey<T>.header(value: ComputedHeaderValue<T>) {
		this.header(value.compute())
	}

	// endregion

	// region Body
	@Suppress("MemberVisibilityCanBePrivate")
	var body: HttpEntity<*>? = null

	fun <T : Any> json(body: T) {
		val byteArray = mapper.writeValueAsBytes(body)
		this.body = HttpEntity(byteArray)
		headers {
			contentLength = byteArray.size.toLong()
			contentType = MediaType.APPLICATION_JSON
		}
	}

	fun urlEncoded(vararg entries: Pair<String, String>) {
		val value = entries.joinToString("&") { "${encodeURIComponent(it.first)}=${encodeURIComponent(it.second)}" }
		this.body = HttpEntity(value)
		headers {
			contentLength = value.length.toLong()
			contentType = MediaType.APPLICATION_FORM_URLENCODED
		}
	}
	// endregion

}

fun <This : Fetch, R> FetchBuilder<*>.thisAs(
	@Suppress("UNUSED_PARAMETER") klass: KFunction<This>,
	block: (@Scoped FetchBuilder<This>).() -> R
): R {
	@Suppress("UNCHECKED_CAST")
	return (this as FetchBuilder<This>).let(block)
}

fun <This : Fetch> FetchBuilder<*>.thisAs(
	@Suppress("UNUSED_PARAMETER") klass: KFunction<This>,
): FetchBuilder<This> {
	@Suppress("UNCHECKED_CAST")
	return (this as FetchBuilder<This>)
}

fun <This : Fetch, R> FetchBuilder<*>.selfAs(
	@Suppress("UNUSED_PARAMETER") klass: KFunction<This>,
	block: (@Scoped This).() -> R
): R {
	@Suppress("UNCHECKED_CAST")
	return (this as FetchBuilder<This>).self.let(block)
}

fun <This : Fetch, R> FetchBuilder<*>.selfAs(
	@Suppress("UNUSED_PARAMETER") klass: KFunction<This>,
): This {
	@Suppress("UNCHECKED_CAST")
	return (this as FetchBuilder<This>).self
}
