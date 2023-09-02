package no.telenor.fetch.wiremock

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.core.WireMockConfiguration
import org.springframework.boot.test.util.TestPropertyValues
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.context.event.ContextClosedEvent

class WiremockContextInitializer : ApplicationContextInitializer<ConfigurableApplicationContext> {
	override fun initialize(applicationContext: ConfigurableApplicationContext) {
		val wm = WireMockServer(WireMockConfiguration().dynamicPort())
		applicationContext.beanFactory.registerSingleton("wm", wm)
		applicationContext.addApplicationListener {
			if (it is ContextClosedEvent) wm.stop()
		}
		wm.start()
		TestPropertyValues.of("externalServerPort=${wm.port()}").applyTo(applicationContext)
	}
}
