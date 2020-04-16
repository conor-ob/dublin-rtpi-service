package io.rtpi.client

import io.reactivex.Single
import io.rtpi.external.aircoach.AircoachStopJson
import io.rtpi.external.irishrail.IrishRailStationResponseXml
import io.rtpi.external.jcdecaux.StationJson
import io.rtpi.external.rtpi.RtpiBusStopInformationResponseJson
import io.rtpi.external.staticdata.StaticDataApi

class NoOpStaticDataApi : StaticDataApi {

    override fun getAircoachStops(): Single<List<AircoachStopJson>> {
        TODO("Not yet implemented")
    }

    override fun getBusEireannStops(): Single<RtpiBusStopInformationResponseJson> {
        TODO("Not yet implemented")
    }

    override fun getDublinBikesDocks(): Single<List<StationJson>> {
        TODO("Not yet implemented")
    }

    override fun getDublinBusStops(): Single<RtpiBusStopInformationResponseJson> {
        TODO("Not yet implemented")
    }

    override fun getGoAheadStops(): Single<RtpiBusStopInformationResponseJson> {
        TODO("Not yet implemented")
    }

    override fun getIrishRailStations(): Single<IrishRailStationResponseXml> {
        TODO("Not yet implemented")
    }

    override fun getLuasStops(): Single<RtpiBusStopInformationResponseJson> {
        TODO("Not yet implemented")
    }
}
