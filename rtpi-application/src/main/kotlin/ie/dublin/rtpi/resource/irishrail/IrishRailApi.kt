package ie.dublin.rtpi.resource.irishrail

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface IrishRailApi {

    @GET("getAllStationsXML")
    fun getAllStationsXml(): Call<IrishRailStationResponseXml>

    @GET("getStationDataByCodeXML")
    fun getStationDataByCodeXml(
        @Query("StationCode") stationCode: String
    ) : Call<IrishRailStationDataResponseXml>

}
