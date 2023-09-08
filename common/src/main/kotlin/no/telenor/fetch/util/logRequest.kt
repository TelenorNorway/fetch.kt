package no.telenor.fetch.util

import no.telenor.fetch.WithExchange
import no.telenor.fetch.WithLogger
import org.slf4j.MDC
import org.slf4j.event.Level


internal fun WithExchange.asLogger() =
	if (!WithLogger::class.java.isAssignableFrom(this::class.java) && this !is WithLogger) {
		null
	} else {
		(this as WithLogger)
	}

internal fun WithExchange.logRequest(extra: Map<String, String?>) {
	if (!WithLogger::class.java.isAssignableFrom(this::class.java)) return
	(this as WithLogger).log(Level.TRACE, extra, "Fetching...")
}

private fun WithLogger.log(level: Level, format: String, vararg arguments: Any?) {
	when (level) {
		Level.TRACE -> log.trace(format, *arguments)
		Level.DEBUG -> log.debug(format, *arguments)
		Level.INFO -> log.info(format, *arguments)
		Level.WARN -> log.warn(format, *arguments)
		Level.ERROR -> log.error(format, *arguments)
	}
}

internal fun WithLogger.log(
	level: Level,
	extra: Map<String, String?>,
	format: String,
	vararg arguments: Any?,
) {
	val mdcToUnset = mutableListOf<String>()
	val mdcToReset = mutableMapOf<String, String?>()
	val currentMdc = MDC.getCopyOfContextMap() ?: emptyMap()
	for ((name, value) in extra) {
		val key = "param.${name}"
		if (currentMdc.containsKey(key)) {
			mdcToReset[key] = currentMdc[key]
		} else {
			mdcToUnset.add(key)
		}
		MDC.put(key, value)
	}

	log(level, format, *arguments)

	mdcToUnset.forEach { MDC.remove(it) }
	mdcToReset.forEach { MDC.put(it.key, it.value) }
}
