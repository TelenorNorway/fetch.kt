@file:Suppress("NOTHING_TO_INLINE")

package no.telenor.fetch

import no.telenor.fetch.util.*
import org.slf4j.event.Level
import org.springframework.http.HttpEntity
import org.springframework.http.ResponseEntity

fun <This : WithExchange, T> This.fetch(
	url: String,
	vararg urlArgs: Pair<String, Any?>,
	block: (@Scoped FetchBuilder<This>).() -> Unit = {},
	responseType: Class<T>
): ResponseEntity<T> {
	val builder = FetchBuilder(this).apply(block)

	if (builder.body == null) {
		builder.body = HttpEntity<Void>(builder.headers)
	} else {
		builder.body!!.headers.addAll(builder.headers)
	}

	val rawParameters = toRawUrlParametersMap(urlArgs)
	rawParameters.putAll(builder.urlParametersAppend)

	val requestUrl = url + if (builder.queryParamMap.isEmpty()) {
		""
	} else {
		builder.queryParamMap.map { "${encodeURIComponent(it.key)}=${it.value}" }
	}

	val requestContext = builderToContext(builder, rawParameters, url)

	logRequest(requestContext)

	val response = restTemplateExchange(
		requestUrl,
		builder.method,
		builder.body,
		responseType,
		encodeUrlParameters(rawParameters)
	)

	response.headers["x-warning"]?.let {
		val warningStr = (it.joinToString(", "))
		this.asLogger()?.log(
			Level.WARN,
			mapOf(
				*requestContext.map { entry -> entry.key to entry.value }.toTypedArray(),
				*responseToContext(response).map { entry -> entry.key to entry.value }.toTypedArray(),
			),
			"{}",
			warningStr
		)
	}

	return response
}

inline fun <reified T> WithExchange.fetch(
	url: String,
	vararg urlArgs: Pair<String, Any?>,
	noinline block: (@Scoped FetchBuilder<WithExchange>).() -> Unit = {},
) = fetch(url, *urlArgs, block = block, responseType = T::class.java)

inline fun <reified T> WithExchange.fetch(
	baseUrl: String,
	url: String,
	vararg urlArgs: Pair<String, Any?>,
	noinline block: (@Scoped FetchBuilder<WithExchange>).() -> Unit = {}
) = fetch("${baseUrl}/${url.removePrefix("/")}", *urlArgs, block = block, responseType = T::class.java)

inline fun <reified T> WithExchange.fetch(
	baseUrl: String,
	url: String,
	noinline block: (@Scoped FetchBuilder<WithExchange>).() -> Unit = {}
) = fetch("${baseUrl}/${url.removePrefix("/")}", block = block, responseType = T::class.java)

inline fun WithExchange.fetch(
	url: String,
	vararg urlArgs: Pair<String, Any?>,
	noinline block: (@Scoped FetchBuilder<WithExchange>).() -> Unit = {},
) = fetch<Unit>(url, *urlArgs, block = block)

inline fun WithExchange.fetch(
	baseUrl: String,
	url: String,
	vararg urlArgs: Pair<String, Any?>,
	noinline block: (@Scoped FetchBuilder<WithExchange>).() -> Unit = {}
) = fetch<Unit>("${baseUrl}/${url.removePrefix("/")}", *urlArgs, block = block)

inline fun WithExchange.fetch(
	baseUrl: String,
	url: String,
	noinline block: (@Scoped FetchBuilder<WithExchange>).() -> Unit = {}
) = fetch<Unit>("${baseUrl}/${url.removePrefix("/")}", block = block)
