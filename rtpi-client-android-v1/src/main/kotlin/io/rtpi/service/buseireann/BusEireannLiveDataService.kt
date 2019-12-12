package io.rtpi.service.buseireann

import io.rtpi.api.LiveTime
import io.rtpi.external.rtpi.RtpiApi
import io.rtpi.external.rtpi.RtpiRealTimeBusInformationJson
import io.rtpi.service.RtpiLiveTimeFactory

class BusEireannLiveDataService(rtpiApi: RtpiApi) : AbstractBusEireannLiveDataService(rtpiApi) {

    override fun createDueTime(
        serverTimestamp: String,
        json: RtpiRealTimeBusInformationJson
    ): LiveTime = RtpiLiveTimeFactory.createLiveTime(serverTimestamp, json)

}
