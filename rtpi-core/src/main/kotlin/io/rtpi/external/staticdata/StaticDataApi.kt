package io.rtpi.external.staticdata

import io.reactivex.Single
import io.rtpi.external.aircoach.AircoachStopJson
import io.rtpi.external.irishrail.IrishRailStationResponseXml
import io.rtpi.external.jcdecaux.StationJson
import io.rtpi.external.rtpi.RtpiBusStopInformationResponseJson
import retrofit2.http.GET

interface StaticDataApi {

    @GET("aircoach/stops.json")
    fun getAircoachStops(): Single<List<AircoachStopJson>>

    @GET("buseireann/stops.json")
    fun getBusEireannStops(): Single<RtpiBusStopInformationResponseJson>

    @GET("dublinbikes/docks.json")
    fun getDublinBikesDocks(): Single<List<StationJson>>

    @GET("dublinbus/dublinbus/stops.json")
    fun getDublinBusStops(): Single<RtpiBusStopInformationResponseJson>

    @GET("dublinbus/goahead/stops.json")
    fun getGoAheadStops(): Single<RtpiBusStopInformationResponseJson>

    @GET("irishrail/stations.xml")
    fun getIrishRailStations(): Single<IrishRailStationResponseXml>

    @GET("luas/stops.json")
    fun getLuasStops(): Single<RtpiBusStopInformationResponseJson>
}
