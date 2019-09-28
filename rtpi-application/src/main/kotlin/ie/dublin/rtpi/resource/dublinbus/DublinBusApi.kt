package ie.dublin.rtpi.resource.dublinbus

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface DublinBusApi {

    @Headers("Content-Type: text/xml", "Accept-Charset: utf-8")
    @POST("/DublinBusRTPIService.asmx")
    fun getAllDestinations(
        @Body body: DublinBusDestinationRequestXml
    ): Call<DublinBusDestinationResponseXml>

    @Headers("Content-Type: text/xml", "Accept-Charset: utf-8")
    @POST("/DublinBusRTPIService.asmx")
    fun getRealTimeStopData(
        @Body body: DublinBusRealTimeStopDataRequestXml
    ): Call<DublinBusRealTimeStopDataResponseXml>

}
