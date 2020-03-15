package io.rtpi

import com.authzee.kotlinguice4.KotlinModule
import com.codahale.metrics.health.HealthCheck
import com.google.inject.Guice
import com.google.inject.Injector
import io.rtpi.resource.AircoachResource
import io.rtpi.resource.BusEireannResource
import io.rtpi.resource.DublinBikesResource
import io.rtpi.resource.DublinBusResource
import io.rtpi.resource.IrishRailResource
import io.rtpi.resource.LuasResource
import io.dropwizard.Application
import io.dropwizard.setup.Environment
import io.rtpi.module.ApplicationModule
import io.rtpi.module.CacheModule
import io.rtpi.module.ExternalApiModule
import io.rtpi.module.HttpClientModule
import io.rtpi.module.RxModule
import io.rtpi.module.SerializationModule

class RtpiServiceApplication : Application<RtpiServiceConfiguration>() {

    override fun run(configuration: RtpiServiceConfiguration, environment: Environment) {
        val injector = createInjector(provideModules(configuration, environment))
        registerResources(environment, injector)
        registerHealthChecks(environment)
    }

    private fun registerResources(environment: Environment, injector: Injector) {
        environment.jersey().register(injector.getInstance(AircoachResource::class.java))
        environment.jersey().register(injector.getInstance(BusEireannResource::class.java))
        environment.jersey().register(injector.getInstance(DublinBikesResource::class.java))
        environment.jersey().register(injector.getInstance(DublinBusResource::class.java))
        environment.jersey().register(injector.getInstance(IrishRailResource::class.java))
        environment.jersey().register(injector.getInstance(LuasResource::class.java))
    }

    private fun registerHealthChecks(environment: Environment) {
        environment.healthChecks().register(
            "HealthCheck", object : HealthCheck() {
                override fun check(): Result {
                    return Result.healthy()
                }
            }
        )
    }

    private fun provideModules(
        configuration: RtpiServiceConfiguration,
        environment: Environment
    ): List<KotlinModule> = listOf(
        ApplicationModule(configuration, environment),
        HttpClientModule(),
        CacheModule(),
        ExternalApiModule(),
        SerializationModule(),
        RxModule()
    )

    private fun createInjector(modules: List<KotlinModule>): Injector {
        val injector = Guice.createInjector(modules)
        injector.injectMembers(this)
        return injector
    }

    override fun getName() = "Dublin RTPI Service"

    companion object {
        @Throws(Exception::class)
        @JvmStatic
        fun main(args: Array<String>) {
            RtpiServiceApplication().run(*args)
        }
    }
}
