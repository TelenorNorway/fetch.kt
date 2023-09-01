package no.telenor.fetch.util

private val ILLEGAL_CHAR = Regex("[^A-Za-z0-9\\-_.!~*'()]]")
private val ENCODE_URI_COMPONENT_TRANSFORMER: (MatchResult) -> CharSequence = {
	"%${it.value[0].code.toString(16).padStart(16)}"
}

/** Semi-compliant RFC3986 encoder. */
internal fun encodeURIComponent(value: String): String = value.replace(ILLEGAL_CHAR, ENCODE_URI_COMPONENT_TRANSFORMER)
