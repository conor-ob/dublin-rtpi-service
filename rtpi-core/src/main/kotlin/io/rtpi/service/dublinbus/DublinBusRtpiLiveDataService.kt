package io.rtpi.service.dublinbus

import com.google.inject.Inject
import io.rtpi.api.DublinBusLiveData
import io.rtpi.api.Operator
import io.rtpi.external.rtpi.RtpiApi
import io.rtpi.external.rtpi.RtpiRealTimeBusInformationJson
import io.rtpi.service.rtpi.AbstractRtpiLiveDataService
import io.rtpi.validation.validate

class DublinBusRtpiLiveDataService @Inject constructor(
    rtpiApi: RtpiApi
) : AbstractRtpiLiveDataService<DublinBusLiveData>(
    rtpiApi = rtpiApi,
    operator = ""
) {

    override fun newLiveDataInstance(timestamp: String, json: RtpiRealTimeBusInformationJson): DublinBusLiveData {
        return DublinBusLiveData(
            liveTime = createDueTime(timestamp, json),
            operator = Operator.parse(json.operator.validate()),
            route = json.route.validate(),
            destination = json.destination.validate(),
            direction = json.direction.validate(),
            origin = json.origin.validate()
        )
    }
}
