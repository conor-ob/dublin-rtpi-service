package io.rtpi.service.dublinbus

import io.rtpi.api.DublinBusLiveData
import io.rtpi.api.Operator
import io.rtpi.external.rtpi.RtpiApi
import io.rtpi.external.rtpi.RtpiRealTimeBusInformationJson
import io.rtpi.service.rtpi.AbstractRtpiLiveDataService

class DublinBusLiveDataService(
    rtpiApi: RtpiApi
) : AbstractRtpiLiveDataService<DublinBusLiveData>(
    rtpiApi = rtpiApi,
    operator = ""
) {

    override fun newLiveDataInstance(timestamp: String, json: RtpiRealTimeBusInformationJson): DublinBusLiveData {
        return DublinBusLiveData(
            liveTime = createDueTime(timestamp, json),
            operator = Operator.parse(json.operator!!.trim()),
            route = json.route!!.trim(),
            destination = json.destination!!.trim(),
            direction = json.direction!!.trim(),
            origin = json.origin!!.trim()
        )
    }

}
