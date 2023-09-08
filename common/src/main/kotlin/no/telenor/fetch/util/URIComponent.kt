package no.telenor.fetch.util

private val ILLEGAL_CHAR = Regex("[^A-Za-z0-9\\-_.!~*'()]")
private val ESCAPED_CHAR = Regex("%[0-9]{2}")
private val ENCODE_URI_COMPONENT_TRANSFORMER: (MatchResult) -> CharSequence = {
	"%${it.value[0].code.toString(16).padStart(16)}"
}

private val DECODE_URI_COMPONENT_TRANSFORMER: (MatchResult) -> CharSequence = {
	it.value.substring(1).toInt(16).toChar().toString()
}

/** Semi-compliant RFC3986 encoder. */
internal fun encodeURIComponent(value: String): String = value.replace(ILLEGAL_CHAR, ENCODE_URI_COMPONENT_TRANSFORMER)
internal fun decodeURIComponent(value: String): String = value.replace(ESCAPED_CHAR, DECODE_URI_COMPONENT_TRANSFORMER)
