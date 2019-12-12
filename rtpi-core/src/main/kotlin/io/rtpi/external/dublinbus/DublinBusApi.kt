package io.rtpi.external.dublinbus

import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface DublinBusApi {

    @Headers("Content-Type: text/xml", "Accept-Charset: utf-8")
    @POST("/DublinBusRTPIService.asmx")
    fun getAllDestinations(
        @Body body: DublinBusDestinationRequestXml
    ): Single<DublinBusDestinationResponseXml>

    @Headers("Content-Type: text/xml", "Accept-Charset: utf-8")
    @POST("/DublinBusRTPIService.asmx")
    fun getRealTimeStopData(
        @Body body: DublinBusRealTimeStopDataRequestXml
    ): Single<DublinBusRealTimeStopDataResponseXml>

}
