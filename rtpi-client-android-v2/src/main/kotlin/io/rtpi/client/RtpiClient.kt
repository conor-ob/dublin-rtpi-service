package io.rtpi.client

import com.google.gson.GsonBuilder
import com.google.gson.TypeAdapter
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import io.rtpi.api.IrishRailLiveData
import io.rtpi.api.Operator
import io.rtpi.api.RtpiApi
import okhttp3.OkHttpClient
import org.threeten.bp.LocalTime
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RtpiClient(okHttpClient: OkHttpClient? = null) {

    private val gson = GsonBuilder()
        .registerTypeAdapter(object : TypeToken<IrishRailLiveData>(){}.type, object :
            TypeAdapter<IrishRailLiveData>() {
            override fun write(out: JsonWriter?, value: IrishRailLiveData?) {

            }

            override fun read(`in`: JsonReader?): IrishRailLiveData {
                return IrishRailLiveData(
                    "Dart",
                    "Malahide",
                    emptyList(),
                    Operator.DART,
                    "Northbound"
                )
            }
        })
        .create()

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
        .addConverterFactory(GsonConverterFactory.create(gson))
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
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
