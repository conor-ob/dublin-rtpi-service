package io.rtpi.service.dublinbus

import io.rtpi.api.LiveTime
import io.rtpi.external.rtpi.RtpiApi
import io.rtpi.external.rtpi.RtpiRealTimeBusInformationJson
import io.rtpi.service.RtpiLiveTimeFactory

class DublinBusLiveDataService(rtpiApi: RtpiApi) : AbstractDublinBusLiveDataService(rtpiApi) {

    override fun createDueTime(
        serverTimestamp: String,
        json: RtpiRealTimeBusInformationJson
    ): LiveTime = RtpiLiveTimeFactory.createLiveTime(serverTimestamp, json)

}
