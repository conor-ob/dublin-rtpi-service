package io.rtpi.client

import com.google.gson.GsonBuilder
import io.reactivex.Single
import io.rtpi.adapter.DurationTypeAdapter
import io.rtpi.adapter.ZonedDateTimeTypeAdapter
import io.rtpi.api.LiveData
import io.rtpi.api.RtpiApi
import io.rtpi.api.Service
import io.rtpi.api.ServiceLocation
import java.time.Duration
import java.time.ZonedDateTime
import java.util.concurrent.TimeUnit
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class RtpiClient(rtpiClientConfiguration: RtpiClientConfiguration) {

    private val rtpiApi = Retrofit.Builder()
        .baseUrl("http://localhost:8080/")
        .client(
            rtpiClientConfiguration.okHttpClient ?: OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .build()
        )
        .addConverterFactory(
            GsonConverterFactory.create(
                GsonBuilder()
                    .registerTypeAdapter(ZonedDateTime::class.java, ZonedDateTimeTypeAdapter())
                    .registerTypeAdapter(Duration::class.java, DurationTypeAdapter())
                    .create()
            )
        )
        .addCallAdapterFactory(
            RxJava2CallAdapterFactory.create()
        )
        .build()
        .create(RtpiApi::class.java)

    private val aircoachClient = AircoachClient(rtpiApi)

    private val busEireannClient = BusEireannClient(rtpiApi)

    private val dublinBikesClient = DublinBikesClient(rtpiApi, rtpiClientConfiguration.dublinBikesApiKey)

    private val dublinBusClient = DublinBusClient(rtpiApi)

    private val irishRailClient = IrishRailClient(rtpiApi)

    private val luasClient = LuasClient(rtpiApi)

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
