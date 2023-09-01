@file:Suppress("unused")

package no.telenor.fetch

import no.telenor.fetch.util.*
import org.springframework.http.HttpEntity
import org.springframework.http.ResponseEntity

interface Fetch : WithRestTemplate

fun <This : Fetch, T> This.fetch(
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

	logRequest(builder.method, requestUrl, rawParameters)

	return restTemplate.exchange(
		requestUrl,
		builder.method,
		builder.body,
		responseType,
		encodeUrlParameters(rawParameters)
	)
}

inline fun <reified T> Fetch.fetch(
	url: String,
	vararg urlArgs: Pair<String, Any?>,
	noinline block: (@Scoped FetchBuilder<Fetch>).() -> Unit = {},
) = fetch(url, *urlArgs, block = block, responseType = T::class.java)

inline fun <reified T> Fetch.fetch(
	baseUrl: String,
	url: String,
	vararg urlArgs: Pair<String, Any?>,
	noinline block: (@Scoped FetchBuilder<Fetch>).() -> Unit = {}
) = fetch("${baseUrl}/${url.removePrefix("/")}", *urlArgs, block = block, responseType = T::class.java)

inline fun <reified T> Fetch.fetch(
	baseUrl: String,
	url: String,
	noinline block: (@Scoped FetchBuilder<Fetch>).() -> Unit = {}
) = fetch("${baseUrl}/${url.removePrefix("/")}", block = block, responseType = T::class.java)
