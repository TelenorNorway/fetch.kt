package no.telenor.fetch.headers

import com.fasterxml.jackson.annotation.JsonProperty
import no.telenor.fetch.util.HeaderKey
import java.time.Instant
import javax.security.auth.login.CredentialExpiredException
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi
import kotlin.math.floor

abstract class AuthenticationValue {
	abstract fun compute(): String
}

open class ConstantAuthenticationValue(private val value: String) : AuthenticationValue() {
	override fun compute(): String = value
}

open class ConstantTypedToken(tokenType: String, value: String) : ConstantAuthenticationValue("$tokenType $value")

@Suppress("UnnecessaryOptInAnnotation")
@OptIn(ExperimentalEncodingApi::class)
class BasicAuthentication(username: String, password: String) :
	ConstantTypedToken("Basic", Base64.encode("${username}:${password}".encodeToByteArray()))

open class BearerToken(value: String) : ConstantTypedToken("Bearer", value)

data class OauthToken(
	@JsonProperty("access_token") val accessToken: String,
	@JsonProperty("token_type") val tokenType: String,
	@JsonProperty("expires_in") val expiresIn: Int,
	val scope: String,
) : AuthenticationValue() {
	val createdAt = floor((Instant.now().toEpochMilli() / 1000).toDouble()).toLong()
	val expiresAt = createdAt + expiresIn
	private val _token = ConstantTypedToken(tokenType, accessToken)

	val token
		get() = if (isExpired()) {
			throw CredentialExpiredException()
		} else {
			_token
		}

	fun isExpired(since: Instant = Instant.now()) = isExpired(floor((since.toEpochMilli() / 1000).toDouble()).toLong())
	fun isExpired(sinceSeconds: Long) = expiresAt >= sinceSeconds

	override fun compute(): String = token.compute()
}

class ComputedOauthToken(private val computeToken: () -> OauthToken) : AuthenticationValue() {
	private val lock = object {}
	private var oauthToken: OauthToken? = null
	private fun <Return> usingToken(block: (OauthToken) -> Return): Return? = synchronized(lock) {
		oauthToken?.let(block)
	}

	val createdAt get() = usingToken { it.createdAt }
	val expiresAt get() = usingToken { it.expiresAt }

	fun isExpired(since: Instant = Instant.now()) = usingToken { it.isExpired(since) } ?: true
	fun isExpired(since: Long) = usingToken { it.isExpired(since) } ?: true

	override fun compute() = synchronized(lock) {
		if (oauthToken == null || isExpired()) oauthToken = computeToken()
		oauthToken!!.compute()
	}
}

val Authorization = HeaderKey<AuthenticationValue>("authorization") { headers, value ->
	headers["authorization"] = value.compute()
}

