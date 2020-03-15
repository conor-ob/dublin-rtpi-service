package io.rtpi

import com.authzee.kotlinguice4.KotlinModule
import com.codahale.metrics.health.HealthCheck
import com.google.inject.Guice
import com.google.inject.Injector
import io.dropwizard.Application
import io.dropwizard.setup.Bootstrap
import io.dropwizard.setup.Environment
import io.dropwizard.views.ViewBundle
import io.rtpi.module.ApplicationModule
import io.rtpi.module.CacheModule
import io.rtpi.module.ExternalApiModule
import io.rtpi.module.HttpClientModule
import io.rtpi.module.RxModule
import io.rtpi.module.SerializationModule
import io.rtpi.resource.LiveDataResource
import io.rtpi.resource.ServiceLocationResource
import io.rtpi.swagger.SwaggerResource
import io.swagger.jaxrs.config.BeanConfig
import io.swagger.jaxrs.listing.ApiListingResource
import io.swagger.jaxrs.listing.SwaggerSerializers

class RtpiServiceApplication : Application<RtpiServiceConfiguration>() {

    override fun initialize(bootstrap: Bootstrap<RtpiServiceConfiguration>) {
        super.initialize(bootstrap)
        bootstrap.addBundle(ViewBundle<RtpiServiceConfiguration>())
    }

    override fun run(configuration: RtpiServiceConfiguration, environment: Environment) {
        val injector = createInjector(provideModules(configuration, environment))
        registerResources(environment, injector)
        registerSwagger(environment)
        registerHealthChecks(environment)
    }

    private fun registerResources(environment: Environment, injector: Injector) {
        environment.jersey().register(injector.getInstance(ServiceLocationResource::class.java))
        environment.jersey().register(injector.getInstance(LiveDataResource::class.java))
    }

    private fun registerSwagger(environment: Environment) {
        val beanConfig = BeanConfig()
        beanConfig.title = name
        beanConfig.version = "0.1.0"
        beanConfig.schemes = arrayOf("https")
        beanConfig.basePath = "/"
        beanConfig.prettyPrint = true
        beanConfig.resourcePackage = "io.rtpi"
        beanConfig.scan = true

        environment.jersey().register(ApiListingResource::class.java)
        environment.jersey().register(SwaggerSerializers::class.java)
        environment.jersey().register(SwaggerResource::class.java)
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
