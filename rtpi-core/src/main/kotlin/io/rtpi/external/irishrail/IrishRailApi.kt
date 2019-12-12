package io.rtpi.external.irishrail

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface IrishRailApi {

    @GET("getAllStationsXML")
    fun getAllStationsXml(): Single<IrishRailStationResponseXml>

    @GET("getStationDataByCodeXML")
    fun getStationDataByCodeXml(
        @Query("StationCode") stationCode: String
    ) : Single<IrishRailStationDataResponseXml>

}
