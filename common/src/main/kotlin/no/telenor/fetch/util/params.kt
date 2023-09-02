package no.telenor.fetch.util

internal fun toRawUrlParametersMap(parameters: Array<out Pair<String, Any?>>): MutableMap<String, String?> =
	mutableMapOf(
		*parameters.map { param ->
			param.first to @Suppress("SimpleRedundantLet") param.second?.let { it.toString() }
		}.toTypedArray()
	)

internal fun encodeUrlParameters(parameters: Map<String, String?>): Map<String, String?> = parameters.map { entry ->
	encodeURIComponent(entry.key) to entry.value?.let { encodeURIComponent(it) }
}.toMap()
