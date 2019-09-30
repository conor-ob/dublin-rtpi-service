package io.rtpi.resource.rtpi

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface RtpiApi {

    @GET("busstopinformation")
    fun busStopInformation(
        @Query("operator") operator: String,
        @Query("format") format: String
    ): Call<RtpiBusStopInformationResponseJson>

    @GET("realtimebusinformation")
    fun realTimeBusInformation(
        @Query("stopid") stopId: String,
        @Query("operator") operator: String,
        @Query("format") format: String
    ): Call<RtpiRealTimeBusInformationResponseJson>
}
