package io.rtpi.resource.aircoach

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface AircoachApi {

    @GET("api/eta/stops/{id}")
    fun getLiveData(
        @Path("id") id: String
    ): Call<ServiceResponseJson>

}
