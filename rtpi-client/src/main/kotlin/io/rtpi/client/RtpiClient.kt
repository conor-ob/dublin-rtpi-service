package io.rtpi.client

import com.google.gson.GsonBuilder
import io.rtpi.adapter.DurationTypeAdapter
import io.rtpi.adapter.ZonedDateTimeTypeAdapter
import io.rtpi.api.RtpiApi
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.time.Duration
import java.time.ZonedDateTime
import java.util.concurrent.TimeUnit

class RtpiClient(okHttpClient: OkHttpClient? = null) {

    private val rtpiApi = Retrofit.Builder()
        .baseUrl("https://dublin-rtpi.herokuapp.com/")
        .client(
            okHttpClient ?: OkHttpClient.Builder()
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

    private val dublinBikesClient = DublinBikesClient(rtpiApi)

    private val dublinBusClient = DublinBusClient(rtpiApi)

    private val irishRailClient = IrishRailClient(rtpiApi)

    private val luasClient = LuasClient(rtpiApi)

    fun aircoach() = aircoachClient

    fun busEireann() = busEireannClient

    fun dublinBikes() = dublinBikesClient

    fun dublinBus() = dublinBusClient

    fun irishRail() = irishRailClient

    fun luas() = luasClient
}
