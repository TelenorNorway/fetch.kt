package no.telenor.fetch.headers

import no.telenor.fetch.util.HeaderKey
import no.telenor.fetch.util.HeaderValueWithString
import org.springframework.http.MediaType

val Accept = HeaderKey<List<MediaType>>("accept") { headers, value ->
	headers.accept = value
}

val Json = HeaderValueWithString(
	Accept,
	listOf(MediaType.APPLICATION_JSON),
	"application/json"
)
