package io.rtpi.client

import io.reactivex.Single
import io.rtpi.api.LiveData
import io.rtpi.api.Service
import io.rtpi.api.ServiceLocation
import io.rtpi.external.aircoach.AircoachApi
import io.rtpi.external.dublinbus.DublinBusApi
import io.rtpi.external.irishrail.IrishRailApi
import io.rtpi.external.jcdecaux.JcDecauxApi
import io.rtpi.external.rtpi.RtpiApi
import io.rtpi.external.staticdata.StaticDataApi
import io.rtpi.service.aircoach.AircoachLiveDataService
import io.rtpi.service.aircoach.AircoachStopService
import io.rtpi.service.aircoach.RhinoEngineAircoachWebScraper
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
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.X509TrustManager

class RtpiClient(rtpiClientConfiguration: RtpiClientConfiguration) {

    private val defaultOkHttpClient: OkHttpClient =
        rtpiClientConfiguration.okHttpClient ?: OkHttpClient.Builder()
            .retryOnConnectionFailure(true)
            .connectTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .build()

    private val sslContext = SSLContext.getInstance("TLS")

    init {
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

    private val aircoachOkHttpClient = newAircoachOkHttpClient(rtpiClientConfiguration.okHttpClient)

    private fun newAircoachOkHttpClient(okHttpClient: OkHttpClient?): OkHttpClient {
        val builder = OkHttpClient.Builder()
            .hostnameVerifier { hostname, session ->
                return@hostnameVerifier hostname == "tracker.aircoach.ie"
                    && session.peerHost == "tracker.aircoach.ie"
                    && session.peerPort == 443
            }
            .sslSocketFactory(sslContext.socketFactory)
            .retryOnConnectionFailure(okHttpClient?.retryOnConnectionFailure() ?: true)
            .connectTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)

        if (!okHttpClient?.networkInterceptors().isNullOrEmpty()) {
            requireNotNull(okHttpClient).networkInterceptors().forEach { interceptor ->
                builder.addInterceptor(interceptor)
            }
        }

        return builder.build()
    }

    private val callAdapterFactory = RxJava2CallAdapterFactory.create()

    private val gsonConverterFactory = GsonConverterFactory.create()

    private val xmlConverterFactory = SimpleXmlConverterFactory.create()

    private val aircoachApi = Retrofit.Builder()
        .baseUrl("https://tracker.aircoach.ie/")
        .client(aircoachOkHttpClient)
        .addCallAdapterFactory(callAdapterFactory)
        .addConverterFactory(gsonConverterFactory)
        .build()
        .create(AircoachApi::class.java)

    private val aircoachWebScraper = RhinoEngineAircoachWebScraper("https://tracker.aircoach.ie/")

    private val jcDecauxApi = Retrofit.Builder()
        .baseUrl("https://api.jcdecaux.com/vls/v1/")
        .client(defaultOkHttpClient)
        .addCallAdapterFactory(callAdapterFactory)
        .addConverterFactory(gsonConverterFactory)
        .build()
        .create(JcDecauxApi::class.java)

    private val dublinBusApi = Retrofit.Builder()
        .baseUrl("http://rtpi.dublinbus.ie/")
        .client(defaultOkHttpClient)
        .addCallAdapterFactory(callAdapterFactory)
        .addConverterFactory(xmlConverterFactory)
        .build()
        .create(DublinBusApi::class.java)

    private val rtpiApi = Retrofit.Builder()
        .baseUrl("https://data.smartdublin.ie/cgi-bin/rtpi/")
        .client(defaultOkHttpClient)
        .addCallAdapterFactory(callAdapterFactory)
        .addConverterFactory(gsonConverterFactory)
        .build()
        .create(RtpiApi::class.java)

    private val rtpiFallbackApi = Retrofit.Builder()
        .baseUrl("https://rtpiapp.rtpi.openskydata.com/RTPIPublicService_V3/service.svc/")
        .client(defaultOkHttpClient)
        .addCallAdapterFactory(callAdapterFactory)
        .addConverterFactory(gsonConverterFactory)
        .build()
        .create(RtpiApi::class.java)

    private val irishRailApi = Retrofit.Builder()
        .baseUrl("http://api.irishrail.ie/realtime/realtime.asmx/")
        .client(defaultOkHttpClient)
        .addCallAdapterFactory(callAdapterFactory)
        .addConverterFactory(xmlConverterFactory)
        .build()
        .create(IrishRailApi::class.java)

    private val staticDataApi = Retrofit.Builder()
        .baseUrl("https://raw.githubusercontent.com/conor-ob/rtpi-static-data/master/")
        .addCallAdapterFactory(callAdapterFactory)
        .addConverterFactory(gsonConverterFactory)
        .addConverterFactory(xmlConverterFactory)
        .build()
        .create(StaticDataApi::class.java)

    private val aircoachClient = AircoachClient(
        AircoachStopService(aircoachWebScraper, staticDataApi),
        AircoachLiveDataService(aircoachApi)
    )

    private val busEireannClient = BusEireannClient(
        BusEireannStopService(rtpiApi, rtpiFallbackApi),
        BusEireannLiveDataService(rtpiApi, rtpiFallbackApi)
    )

    private val dublinBikesClient = DublinBikesClient(
        DublinBikesDockService(jcDecauxApi),
        DublinBikesLiveDataService(jcDecauxApi),
        rtpiClientConfiguration.dublinBikesApiKey
    )

    private val dublinBusClient = DublinBusClient(
        DublinBusStopService(dublinBusApi, rtpiApi, rtpiFallbackApi),
        DublinBusLiveDataService(dublinBusApi, rtpiApi, rtpiFallbackApi)
    )

    private val irishRailClient = IrishRailClient(
        IrishRailStationService(irishRailApi),
        IrishRailLiveDataService(irishRailApi)
    )

    private val luasClient = LuasClient(
        LuasStopService(rtpiApi, rtpiFallbackApi),
        LuasLiveDataService(rtpiApi, rtpiFallbackApi)
    )

    fun getServiceLocations(service: Service): Single<List<ServiceLocation>> {
        return when (service) {
            Service.AIRCOACH -> aircoachClient.getStops()
            Service.BUS_EIREANN -> busEireannClient.getStops()
            Service.DUBLIN_BIKES -> dublinBikesClient.getDocks()
            Service.DUBLIN_BUS -> dublinBusClient.getStops()
            Service.IRISH_RAIL -> irishRailClient.getStations()
            Service.LUAS -> luasClient.getStops()
        }
    }

    fun getLiveData(service: Service, locationId: String): Single<List<LiveData>> {
        return when (service) {
            Service.AIRCOACH -> aircoachClient.getLiveData(locationId)
            Service.BUS_EIREANN -> busEireannClient.getLiveData(locationId)
            Service.DUBLIN_BIKES -> dublinBikesClient.getLiveData(locationId).map { listOf(it) }
            Service.DUBLIN_BUS -> dublinBusClient.getLiveData(locationId)
            Service.IRISH_RAIL -> irishRailClient.getLiveData(locationId)
            Service.LUAS -> luasClient.getLiveData(locationId)
        }
    }
}
