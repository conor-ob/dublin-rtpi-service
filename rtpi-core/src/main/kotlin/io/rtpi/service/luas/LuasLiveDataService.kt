package io.rtpi.service.luas

import com.google.inject.Inject
import io.rtpi.api.LuasLiveData
import io.rtpi.api.Operator
import io.rtpi.external.rtpi.RtpiApi
import io.rtpi.external.rtpi.RtpiRealTimeBusInformationJson
import io.rtpi.service.rtpi.AbstractRtpiLiveDataService

class LuasLiveDataService @Inject constructor(
    rtpiApi: RtpiApi
) : AbstractRtpiLiveDataService<LuasLiveData>(
    rtpiApi = rtpiApi,
    operator = Operator.LUAS.shortName
) {

    override fun newLiveDataInstance(timestamp: String, json: RtpiRealTimeBusInformationJson): LuasLiveData {
        return LuasLiveData(
            liveTime = createDueTime(timestamp, json),
            operator = Operator.parse(json.operator!!.trim()),
            route = json.route!!.trim(),
            destination = json.destination!!.replace("LUAS ", "").trim(),
            direction = json.direction!!.trim(),
            origin = json.origin!!.replace("LUAS ", "").trim()
        )
    }

}
