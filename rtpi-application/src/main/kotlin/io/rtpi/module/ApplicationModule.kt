package io.rtpi.module

import com.authzee.kotlinguice4.KotlinModule
import io.dropwizard.setup.Environment
import io.rtpi.RtpiServiceConfiguration

class ApplicationModule(
    private val configuration: RtpiServiceConfiguration,
    private val environment: Environment
) : KotlinModule() {

    override fun configure() {
        bind<RtpiServiceConfiguration>().toInstance(configuration)
        bind<Environment>().toInstance(environment)
    }
}
