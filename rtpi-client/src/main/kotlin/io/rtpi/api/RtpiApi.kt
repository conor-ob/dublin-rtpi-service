package io.rtpi.api

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface RtpiApi {

    @GET("aircoach/locations")
    fun getAircoachStops(): Single<List<StopLocation>>

    @GET("aircoach/livedata")
    fun getAircoachLiveData(
        @Query(value = "locationId") stopId: String
    ): Single<List<PredictionLiveData>>

    @GET("buseireann/locations")
    fun getBusEireannStops(): Single<List<StopLocation>>

    @GET("buseireann/livedata")
    fun getBusEireannLiveData(
        @Query(value = "locationId") stopId: String
    ): Single<List<PredictionLiveData>>

    @GET("dublinbikes/locations")
    fun getDublinBikesDocks(
        @Query(value = "apiKey") apiKey: String
    ): Single<List<DockLocation>>

    @GET("dublinbikes/livedata")
    fun getDublinBikesLiveData(
        @Query(value = "locationId") dockId: String,
        @Query(value = "apiKey") apiKey: String
    ): Single<DockLiveData>

    @GET("dublinbus/locations")
    fun getDublinBusStops(): Single<List<StopLocation>>

    @GET("dublinbus/livedata")
    fun getDublinBusLiveData(
        @Query(value = "locationId") stopId: String
    ): Single<List<PredictionLiveData>>

    @GET("irishrail/locations")
    fun getIrishRailStations(): Single<List<StopLocation>>

    @GET("irishrail/livedata")
    fun getIrishRailLiveData(
        @Query(value = "locationId") stationId: String
    ): Single<List<PredictionLiveData>>

    @GET("luas/locations")
    fun getLuasStops(): Single<List<StopLocation>>

    @GET("luas/livedata")
    fun getLuasLiveData(
        @Query(value = "locationId") stopId: String
    ): Single<List<PredictionLiveData>>
}
