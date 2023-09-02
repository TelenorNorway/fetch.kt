package no.telenor.fetch.wiremock.stubs

import com.github.tomakehurst.wiremock.client.MappingBuilder
import com.github.tomakehurst.wiremock.client.WireMock.aResponse
import com.github.tomakehurst.wiremock.client.WireMock.get
import no.telenor.fetch.Foo
import no.telenor.fetch.wiremock.mapper

val fooOkStub: MappingBuilder = get("/foo").willReturn(
	aResponse()
		.withStatus(200)
		.withHeader("content-type", "application/json")
		.withBody(mapper.writeValueAsString(Foo()))
)
