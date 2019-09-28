package ie.dublin.rtpi.resource.jcdecaux

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface JcDecauxApi {

    @GET("stations")
    fun stations(
        @Query("contract") contract: String,
        @Query("apiKey") apiKey: String
    ): Call<List<StationJson>>

    @GET("stations/{station_number}")
    fun station(
        @Path("station_number") stationNumber: String,
        @Query("contract") contract: String,
        @Query("apiKey") apiKey: String
    ): Call<StationJson>

}
