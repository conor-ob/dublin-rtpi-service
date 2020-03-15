package io.rtpi.service.buseireann

import com.google.inject.Inject
import io.rtpi.api.LiveTime
import io.rtpi.external.rtpi.RtpiApi
import io.rtpi.external.rtpi.RtpiRealTimeBusInformationJson
import io.rtpi.service.RtpiLiveTimeFactory

class BusEireannLiveDataService @Inject constructor(rtpiApi: RtpiApi) : AbstractBusEireannLiveDataService(rtpiApi) {

    override fun createDueTime(serverTimestamp: String, json: RtpiRealTimeBusInformationJson): LiveTime {
        return RtpiLiveTimeFactory.createLiveTime(serverTimestamp, json)
    }

}
