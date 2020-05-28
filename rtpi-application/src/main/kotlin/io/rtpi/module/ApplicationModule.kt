package io.rtpi.module

import com.authzee.kotlinguice4.KotlinModule
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import io.dropwizard.setup.Environment
import io.rtpi.RtpiServiceConfiguration

class ApplicationModule(
    private val configuration: RtpiServiceConfiguration,
    private val environment: Environment
) : KotlinModule() {

    override fun configure() {
        bind<RtpiServiceConfiguration>().toInstance(configuration)
        bind<Environment>().toInstance(environment)
        environment.objectMapper
            .registerModule(JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .disable(SerializationFeature.WRITE_DURATIONS_AS_TIMESTAMPS)
    }
}
