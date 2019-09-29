package io.rtpi.api

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface RtpiApi {

    @GET("aircoach/stops")
    fun getAircoachStops(): Single<List<AircoachStop>>

    @GET("aircoach/livedata")
    fun getAircoachLiveData(
        @Query(value = "stopId") stopId: String,
        @Query(value = "compact") compact: Boolean
    ): Single<List<AircoachLiveData>>

    @GET("buseireann/stops")
    fun getBusEireannStops(): Single<List<BusEireannStop>>

    @GET("buseireann/livedata")
    fun getBusEireannLiveData(
        @Query(value = "stopId") stopId: String,
        @Query(value = "compact") compact: Boolean
    ): Single<List<BusEireannLiveData>>

    @GET("dublinbikes/docks")
    fun getDublinBikesDocks(): Single<List<DublinBikesDock>>

    @GET("dublinbikes/livedata")
    fun getDublinBikesLiveData(
        @Query(value = "dockId") dockId: String,
        @Query(value = "compact") compact: Boolean
    ): Single<List<DublinBikesLiveData>>

    @GET("dublinbus/stops")
    fun getDublinBusStops(): Single<List<DublinBusStop>>

    @GET("dublinbus/livedata")
    fun getDublinBusLiveData(
        @Query(value = "stopId") stopId: String,
        @Query(value = "compact") compact: Boolean
    ): Single<List<DublinBusLiveData>>

    @GET("irishrail/stations")
    fun getIrishRailStations(): Single<List<IrishRailStation>>

    @GET("irishrail/livedata")
    fun getIrishRailLiveData(
        @Query(value = "stationId") stationId: String,
        @Query(value = "compact") compact: Boolean
    ): Single<List<IrishRailLiveData>>

    @GET("luas/stops")
    fun getLuasStops(): Single<List<LuasStop>>

    @GET("luas/livedata")
    fun getLuasLiveData(
        @Query(value = "stopId") stopId: String,
        @Query(value = "compact") compact: Boolean
    ): Single<List<LuasLiveData>>

}
