package no.telenor.fetch.util

open class HeaderValue<Type>(
	@Suppress("UNUSED_PARAMETER") key: HeaderKey<Type>,
	internal val value: Type
)

class HeaderValueWithString<Type>(
	key: HeaderKey<Type>,
	value: Type,
	internal val stringValue: String,
) : HeaderValue<Type>(key, value)

abstract class ComputedHeaderValue<Type>(@Suppress("UNUSED_PARAMETER") key: HeaderKey<Type>) {
	abstract fun compute(): Type
}

class ComputeHeaderValue<Type>(key: HeaderKey<Type>, private val valueCompute: () -> Type) :
	ComputedHeaderValue<Type>(key) {
	override fun compute(): Type = this.valueCompute()
}
