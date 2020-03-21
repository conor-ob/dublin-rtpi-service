package io.rtpi.service.buseireann

import com.google.inject.Inject
import io.rtpi.api.BusEireannLiveData
import io.rtpi.api.Operator
import io.rtpi.external.rtpi.RtpiApi
import io.rtpi.external.rtpi.RtpiRealTimeBusInformationJson
import io.rtpi.service.rtpi.AbstractRtpiLiveDataService

class BusEireannLiveDataService @Inject constructor(
    rtpiApi: RtpiApi
) : AbstractRtpiLiveDataService<BusEireannLiveData>(
    rtpiApi = rtpiApi,
    operator = Operator.BUS_EIREANN.shortName
) {

    override fun newLiveDataInstance(timestamp: String, json: RtpiRealTimeBusInformationJson): BusEireannLiveData {
        return BusEireannLiveData(
            liveTime = createDueTime(timestamp, json),
            operator = Operator.parse(json.operator!!.trim()),
            route = json.route!!.trim(),
            destination = json.destination!!.trim(),
            direction = json.direction!!.trim(),
            origin = json.origin!!.trim()
        )
    }

}
