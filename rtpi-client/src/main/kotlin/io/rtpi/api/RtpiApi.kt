package io.rtpi.api

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface RtpiApi {

    @GET("aircoach/locations")
    fun getAircoachStops(): Single<List<ServiceLocation>>

    @GET("aircoach/livedata")
    fun getAircoachLiveData(
        @Query(value = "locationId") stopId: String
    ): Single<List<LiveData>>

    @GET("buseireann/locations")
    fun getBusEireannStops(): Single<List<ServiceLocation>>

    @GET("buseireann/livedata")
    fun getBusEireannLiveData(
        @Query(value = "locationId") stopId: String
    ): Single<List<LiveData>>

    @GET("dublinbikes/locations")
    fun getDublinBikesDocks(
        @Query(value = "apiKey") apiKey: String
    ): Single<List<ServiceLocation>>

    @GET("dublinbikes/livedata")
    fun getDublinBikesLiveData(
        @Query(value = "locationId") dockId: String,
        @Query(value = "apiKey") apiKey: String
    ): Single<LiveData>

    @GET("dublinbus/locations")
    fun getDublinBusStops(): Single<List<ServiceLocation>>

    @GET("dublinbus/livedata")
    fun getDublinBusLiveData(
        @Query(value = "locationId") stopId: String
    ): Single<List<LiveData>>

    @GET("irishrail/locations")
    fun getIrishRailStations(): Single<List<ServiceLocation>>

    @GET("irishrail/livedata")
    fun getIrishRailLiveData(
        @Query(value = "locationId") stationId: String
    ): Single<List<LiveData>>

    @GET("luas/locations")
    fun getLuasStops(): Single<List<ServiceLocation>>

    @GET("luas/livedata")
    fun getLuasLiveData(
        @Query(value = "locationId") stopId: String
    ): Single<List<LiveData>>
}
