package no.telenor.fetch.util

import org.springframework.http.HttpHeaders

class HeaderKey<Type>(
	internal val name: String,
	internal val applyHeader: (headers: HttpHeaders, value: Type) -> Unit
)
