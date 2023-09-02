package no.telenor.fetch.util

import no.telenor.fetch.WithExchange
import no.telenor.fetch.WithLogger
import org.slf4j.MDC
import org.springframework.http.HttpMethod

internal fun WithExchange.logRequest(
	method: HttpMethod,
	url: String,
	parameters: Map<String, String?>
) {
	if (!WithLogger::class.java.isAssignableFrom(this::class.java)) return

	val mdcToUnset = mutableListOf<String>()
	val mdcToReset = mutableMapOf<String, String?>()
	val currentMdc = MDC.getCopyOfContextMap() ?: emptyMap()
	for ((name, value) in parameters) {
		val key = "param.${name}"
		if (currentMdc.containsKey(key)) {
			mdcToReset[key] = currentMdc[key]
		} else {
			mdcToUnset.add(key)
		}
		MDC.put(key, value)
	}

	(this as WithLogger).log.trace("HTTP {} {}", method.name(), url)

	mdcToUnset.forEach { MDC.remove(it) }
	mdcToReset.forEach { MDC.put(it.key, it.value) }
}
