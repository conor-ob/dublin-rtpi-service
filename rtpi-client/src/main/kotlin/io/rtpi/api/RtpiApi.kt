package io.rtpi.api

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface RtpiApi {

    @GET("aircoach/locations")
    fun getAircoachStops(): Single<List<AircoachStop>>

    @GET("aircoach/livedata")
    fun getAircoachLiveData(
        @Query(value = "locationId") stopId: String
    ): Single<List<AircoachLiveData>>

    @GET("buseireann/locations")
    fun getBusEireannStops(): Single<List<BusEireannStop>>

    @GET("buseireann/livedata")
    fun getBusEireannLiveData(
        @Query(value = "locationId") stopId: String
    ): Single<List<BusEireannLiveData>>

    @GET("dublinbikes/locations")
    fun getDublinBikesDocks(
        @Query(value = "apiKey") apiKey: String
    ): Single<List<DublinBikesDock>>

    @GET("dublinbikes/livedata")
    fun getDublinBikesLiveData(
        @Query(value = "locationId") dockId: String,
        @Query(value = "apiKey") apiKey: String
    ): Single<DublinBikesLiveData>

    @GET("dublinbus/locations")
    fun getDublinBusStops(): Single<List<DublinBusStop>>

    @GET("dublinbus/livedata")
    fun getDublinBusLiveData(
        @Query(value = "locationId") stopId: String
    ): Single<List<DublinBusLiveData>>

    @GET("irishrail/locations")
    fun getIrishRailStations(): Single<List<IrishRailStation>>

    @GET("irishrail/livedata")
    fun getIrishRailLiveData(
        @Query(value = "locationId") stationId: String
    ): Single<List<IrishRailLiveData>>

    @GET("luas/locations")
    fun getLuasStops(): Single<List<LuasStop>>

    @GET("luas/livedata")
    fun getLuasLiveData(
        @Query(value = "locationId") stopId: String
    ): Single<List<LuasLiveData>>
}
