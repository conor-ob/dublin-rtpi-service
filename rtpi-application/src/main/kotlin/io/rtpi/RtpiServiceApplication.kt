package io.rtpi

import com.codahale.metrics.health.HealthCheck
import io.rtpi.external.dublinbus.DublinBusApi
import io.rtpi.external.irishrail.IrishRailApi
import io.rtpi.external.jcdecaux.JcDecauxApi
import io.rtpi.external.rtpi.RtpiApi
import io.rtpi.service.buseireann.BusEireannLiveDataService
import io.rtpi.service.buseireann.BusEireannStopService
import io.rtpi.service.dublinbikes.DublinBikesDockService
import io.rtpi.service.dublinbikes.DublinBikesLiveDataService
import io.rtpi.service.dublinbus.DublinBusLiveDataService
import io.rtpi.service.dublinbus.DublinBusStopService
import io.rtpi.service.irishrail.IrishRailLiveDataService
import io.rtpi.service.irishrail.IrishRailStationService
import io.rtpi.service.luas.LuasLiveDataService
import io.rtpi.service.luas.LuasStopService
import io.rtpi.resource.AircoachResource
import io.rtpi.resource.BusEireannResource
import io.rtpi.resource.DublinBikesResource
import io.rtpi.resource.DublinBusResource
import io.rtpi.resource.IrishRailResource
import io.rtpi.resource.LuasResource
import io.rtpi.external.aircoach.AircoachApi
import io.rtpi.service.aircoach.AircoachLiveDataService
import io.rtpi.service.aircoach.AircoachStopService
import io.dropwizard.Application
import io.dropwizard.setup.Environment
import io.rtpi.service.aircoach.JsoupAircoachWebScraper
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
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
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(gsonConverterFactory)
            .build()
            .create(AircoachApi::class.java)

        val aircoachWebScraper = JsoupAircoachWebScraper(
            configuration.apiConfig.aircoachBaseUrl!!
        )

//        val dublinBusApi = Retrofit.Builder()
//            .baseUrl(configuration.apiConfig.dublinBusBaseUrl!!)
//            .client(client)
//            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//            .addConverterFactory(xmlConverterFactory)
//            .build()
//            .create(DublinBusApi::class.java)

        val jcDecauxApi = Retrofit.Builder()
            .baseUrl(configuration.apiConfig.jcDecauxBaseUrl!!)
            .client(client)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(gsonConverterFactory)
            .build()
            .create(JcDecauxApi::class.java)

        val rtpiApi = Retrofit.Builder()
            .baseUrl(configuration.apiConfig.rtpiBaseUrl!!)
            .client(client)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(gsonConverterFactory)
            .build()
            .create(RtpiApi::class.java)

        val irishRailApi = Retrofit.Builder()
            .baseUrl(configuration.apiConfig.irishRailBaseUrl!!)
            .client(client)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
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
                DublinBikesDockService(
                    jcDecauxApi
                ),
                DublinBikesLiveDataService(
                    jcDecauxApi
                )
            )
        )
        environment.jersey().register(
            DublinBusResource(
                DublinBusStopService(rtpiApi),
                DublinBusLiveDataService(rtpiApi)
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
