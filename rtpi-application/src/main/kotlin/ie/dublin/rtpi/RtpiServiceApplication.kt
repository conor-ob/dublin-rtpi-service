package ie.dublin.rtpi

import com.codahale.metrics.health.HealthCheck
import ie.dublin.rtpi.resource.dublinbus.DublinBusApi
import ie.dublin.rtpi.resource.irishrail.IrishRailApi
import ie.dublin.rtpi.resource.jcdecaux.JcDecauxApi
import ie.dublin.rtpi.resource.rtpi.RtpiApi
import ie.dublin.rtpi.service.buseireann.BusEireannLiveDataService
import ie.dublin.rtpi.service.buseireann.BusEireannStopService
import ie.dublin.rtpi.service.dublinbikes.DublinBikesDockService
import ie.dublin.rtpi.service.dublinbikes.DublinBikesLiveDataService
import ie.dublin.rtpi.service.dublinbus.DublinBusLiveDataService
import ie.dublin.rtpi.service.dublinbus.DublinBusStopService
import ie.dublin.rtpi.service.irishrail.IrishRailLiveDataService
import ie.dublin.rtpi.service.irishrail.IrishRailStationService
import ie.dublin.rtpi.service.luas.LuasLiveDataService
import ie.dublin.rtpi.service.luas.LuasStopService
import ie.dublin.rtpi.resource.AircoachResource
import ie.dublin.rtpi.resource.BusEireannResource
import ie.dublin.rtpi.resource.DublinBikesResource
import ie.dublin.rtpi.resource.DublinBusResource
import ie.dublin.rtpi.resource.IrishRailResource
import ie.dublin.rtpi.resource.LuasResource
import ie.dublin.rtpi.resource.aircoach.AircoachApi
import ie.dublin.rtpi.resource.aircoach.JsoupAircoachWebScraper
import ie.dublin.rtpi.service.aircoach.AircoachLiveDataService
import ie.dublin.rtpi.service.aircoach.AircoachStopService
import io.dropwizard.Application
import io.dropwizard.setup.Environment
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.X509TrustManager

class RtpiServiceApplication : Application<RtpiServiceConfiguration>() {

    companion object {
        @Throws(Exception::class)
        @JvmStatic
        fun main(args: Array<String>) {
            RtpiServiceApplication().run(*args)
        }
    }

    override fun run(configuration: RtpiServiceConfiguration, environment: Environment) {
        val client = OkHttpClient.Builder()
            .retryOnConnectionFailure(true)
            .connectTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .build()

        val sslContext = SSLContext.getInstance("TLS")
        sslContext.init(
            null,
            arrayOf<X509TrustManager>(object : X509TrustManager {
                override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {
                }

                override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {
                }

                override fun getAcceptedIssuers(): Array<X509Certificate> {
                    return  emptyArray()
                }
            }),
            SecureRandom()
        )

        val aircoachClient = OkHttpClient.Builder()
            .hostnameVerifier { hostname, session ->
                return@hostnameVerifier hostname == "tracker.aircoach.ie"
                    && session.peerHost == "tracker.aircoach.ie"
                    && session.peerPort == 443
            }
            .sslSocketFactory(sslContext.socketFactory)
            .retryOnConnectionFailure(true)
            .connectTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .build()

        val gsonConverterFactory = GsonConverterFactory.create()
        val xmlConverterFactory = SimpleXmlConverterFactory.create()

        val aircoachApi = Retrofit.Builder()
            .baseUrl(configuration.apiConfig.aircoachBaseUrl!!)
            .client(aircoachClient)
            .addConverterFactory(gsonConverterFactory)
            .build()
            .create(AircoachApi::class.java)

        val aircoachWebScraper = JsoupAircoachWebScraper(
            configuration.apiConfig.aircoachBaseUrl!!
        )

        val dublinBusApi = Retrofit.Builder()
            .baseUrl(configuration.apiConfig.dublinBusBaseUrl!!)
            .client(client)
            .addConverterFactory(xmlConverterFactory)
            .build()
            .create(DublinBusApi::class.java)

        val jcDecauxApi = Retrofit.Builder()
            .baseUrl(configuration.apiConfig.jcDecauxBaseUrl!!)
            .client(client)
            .addConverterFactory(gsonConverterFactory)
            .build()
            .create(JcDecauxApi::class.java)

        val rtpiApi = Retrofit.Builder()
            .baseUrl(configuration.apiConfig.rtpiBaseUrl!!)
            .client(client)
            .addConverterFactory(gsonConverterFactory)
            .build()
            .create(RtpiApi::class.java)

        val irishRailApi = Retrofit.Builder()
            .baseUrl(configuration.apiConfig.irishRailBaseUrl!!)
            .client(client)
            .addConverterFactory(xmlConverterFactory)
            .build()
            .create(IrishRailApi::class.java)

        environment.jersey().register(
            AircoachResource(
                AircoachStopService(aircoachWebScraper),
                AircoachLiveDataService(aircoachApi)
            )
        )
        environment.jersey().register(
            BusEireannResource(
                BusEireannStopService(rtpiApi),
                BusEireannLiveDataService(rtpiApi)
            )
        )
        environment.jersey().register(
            DublinBikesResource(
                DublinBikesDockService(jcDecauxApi, configuration.apiConfig.jcDecauxApiKey!!),
                DublinBikesLiveDataService(jcDecauxApi, configuration.apiConfig.jcDecauxApiKey!!)
            )
        )
        environment.jersey().register(
            DublinBusResource(
                DublinBusStopService(dublinBusApi, rtpiApi),
                DublinBusLiveDataService(dublinBusApi, rtpiApi)
            )
        )
        environment.jersey().register(
            IrishRailResource(
                IrishRailStationService(irishRailApi),
                IrishRailLiveDataService(irishRailApi)
            )
        )
        environment.jersey().register(
            LuasResource(
                LuasStopService(rtpiApi),
                LuasLiveDataService(rtpiApi)
            )
        )

        environment.healthChecks().register("HealthCheck", object : HealthCheck() {
            override fun check(): Result {
                return Result.healthy()
            }
        })
    }

    override fun getName(): String {
        return "Dublin RTPI Service"
    }

}
