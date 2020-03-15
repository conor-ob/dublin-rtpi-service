package io.rtpi.service.luas

import com.google.inject.Inject
import io.rtpi.api.LiveTime
import io.rtpi.external.rtpi.RtpiApi
import io.rtpi.external.rtpi.RtpiRealTimeBusInformationJson
import io.rtpi.service.RtpiLiveTimeFactory

class LuasLiveDataService @Inject constructor(rtpiApi: RtpiApi) : AbstractLuasLiveDataService(rtpiApi) {

    override fun createDueTime(serverTimestamp: String, json: RtpiRealTimeBusInformationJson): LiveTime {
        return RtpiLiveTimeFactory.createLiveTime(serverTimestamp, json)
    }

}
