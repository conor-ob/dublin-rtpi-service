package io.rtpi.client

import io.rtpi.resource.aircoach.AircoachApi
import io.rtpi.resource.aircoach.AircoachWebScraper
import io.rtpi.resource.dublinbus.DublinBusApi
import io.rtpi.resource.irishrail.IrishRailApi
import io.rtpi.resource.jcdecaux.JcDecauxApi
import io.rtpi.resource.rtpi.RtpiApi
import io.rtpi.service.aircoach.AircoachLiveDataService
import io.rtpi.service.aircoach.AircoachStopService
import io.rtpi.service.aircoach.JsoupAircoachWebScraper
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
import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.X509TrustManager

class RtpiClient(okHttpClient: OkHttpClient? = null) {

    private val defaultOkHttpClient: OkHttpClient =
        okHttpClient ?: OkHttpClient.Builder()
            .retryOnConnectionFailure(true)
            .connectTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .build()

    private val sslContext: SSLContext by lazy {
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
        sslContext
    }

    private val aircoachOkHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
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
    }

    private val gsonConverterFactory: Converter.Factory by lazy { GsonConverterFactory.create() }

    private val xmlConverterFactory: Converter.Factory by lazy { SimpleXmlConverterFactory.create() }

    private val aircoachApi: AircoachApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://tracker.aircoach.ie/")
            .client(aircoachOkHttpClient)
            .addConverterFactory(gsonConverterFactory)
            .build()
            .create(AircoachApi::class.java)
    }

    private val aircoachWebScraper: AircoachWebScraper by lazy {
        JsoupAircoachWebScraper("https://tracker.aircoach.ie/")
    }

    private val dublinBusApi: DublinBusApi by lazy {
        Retrofit.Builder()
            .baseUrl("http://rtpi.dublinbus.ie/")
            .client(defaultOkHttpClient)
            .addConverterFactory(xmlConverterFactory)
            .build()
            .create(DublinBusApi::class.java)
    }

    private val jcDecauxApi: JcDecauxApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.jcdecaux.com/vls/v1/")
            .client(defaultOkHttpClient)
            .addConverterFactory(gsonConverterFactory)
            .build()
            .create(JcDecauxApi::class.java)
    }

    private val rtpiApi: RtpiApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://rtpiapp.rtpi.openskydata.com/RTPIPublicService_V3/service.svc/")
            .client(defaultOkHttpClient)
            .addConverterFactory(gsonConverterFactory)
            .build()
            .create(RtpiApi::class.java)
    }

    private val irishRailApi: IrishRailApi by lazy {
        Retrofit.Builder()
            .baseUrl("http://api.irishrail.ie/realtime/realtime.asmx/")
            .client(defaultOkHttpClient)
            .addConverterFactory(xmlConverterFactory)
            .build()
            .create(IrishRailApi::class.java)
    }

    private val aircoachClient: AircoachClient by lazy {
        AircoachClient(
            AircoachStopService(aircoachWebScraper),
            AircoachLiveDataService(aircoachApi)
        )
    }

    private val busEireannClient: BusEireannClient by lazy {
        BusEireannClient(
            BusEireannStopService(rtpiApi),
            BusEireannLiveDataService(rtpiApi)
        )
    }

    private val dublinBikesClient: DublinBikesClient by lazy {
        DublinBikesClient(
            DublinBikesDockService(jcDecauxApi),
            DublinBikesLiveDataService(jcDecauxApi)
        )
    }

    private val dublinBusClient: DublinBusClient by lazy {
        DublinBusClient(
            DublinBusStopService(dublinBusApi, rtpiApi),
            DublinBusLiveDataService(dublinBusApi, rtpiApi)
        )
    }

    private val irishRailClient: IrishRailClient by lazy {
        IrishRailClient(
            IrishRailStationService(irishRailApi),
            IrishRailLiveDataService(irishRailApi)
        )
    }

    private val luasClient: LuasClient by lazy {
        LuasClient(
            LuasStopService(rtpiApi),
            LuasLiveDataService(rtpiApi)
        )
    }

    fun aircoach() = aircoachClient

    fun busEireann() = busEireannClient

    fun dublinBikes() = dublinBikesClient

    fun dublinBus() = dublinBusClient

    fun irishRail() = irishRailClient

    fun luas() = luasClient

}
