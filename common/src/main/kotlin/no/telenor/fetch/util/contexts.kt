package no.telenor.fetch.util

import no.telenor.fetch.FetchBuilder
import org.springframework.http.ResponseEntity

internal fun builderToContext(
	builder: FetchBuilder<*>,
	rawParams: Map<String, String?>,
	requestUrl: String,
): Map<String, String?> {
	val parameters = mutableMapOf<String, String?>()

	parameters["request.url"] = requestUrl
	parameters["request.method"] = builder.method.toString()
	builder.headers.forEach {
		parameters["request.header.${it.key}"] = if (it.key.lowercase() == "authorization") {
			"***"
		} else {
			it.value.joinToString(", ")
		}
	}

	val reverseQueryNameLookupTable = mapOf(*builder.queryParamMap.map { it.value to it.key }.toTypedArray())

	val queryParameters = mutableMapOf<String, String?>()
	val pathParameters = mutableMapOf<String, String?>()

	builder.queryParamMap.forEach {
		queryParameters[it.key] = rawParams[it.value]
	}

	rawParams.forEach {
		if (reverseQueryNameLookupTable.containsKey(it.key)) return@forEach
		pathParameters[it.key] = it.value
	}

	queryParameters.forEach {
		parameters["request.query.${it.key}"] = it.value
	}

	pathParameters.forEach {
		parameters["request.parameter.${it.key}"] = it.value
	}

	return parameters
}

internal fun responseToContext(response: ResponseEntity<*>): Map<String, String?> {
	val parameters = mutableMapOf<String, String?>()

	parameters["response.status"] = response.statusCode.value().toString()
	
	response.headers.map {
		if (it.key.lowercase() == "x-warning") return@map
		"${it.key}=${it.value.joinToString(", ")}"
	}

	return parameters
}
