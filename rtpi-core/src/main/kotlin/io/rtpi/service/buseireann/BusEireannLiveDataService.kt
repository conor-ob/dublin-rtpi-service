package io.rtpi.service.buseireann

import com.google.inject.Inject
import io.rtpi.api.BusEireannLiveData
import io.rtpi.api.Operator
import io.rtpi.external.rtpi.RtpiApi
import io.rtpi.external.rtpi.RtpiRealTimeBusInformationJson
import io.rtpi.service.rtpi.AbstractRtpiLiveDataService
import io.rtpi.validation.validate

class BusEireannLiveDataService @Inject constructor(
    rtpiApi: RtpiApi
) : AbstractRtpiLiveDataService<BusEireannLiveData>(
    rtpiApi = rtpiApi,
    operator = Operator.BUS_EIREANN.shortName
) {

    override fun newLiveDataInstance(timestamp: String, json: RtpiRealTimeBusInformationJson): BusEireannLiveData {
        return BusEireannLiveData(
            liveTime = createDueTime(timestamp, json),
            operator = Operator.parse(json.operator.validate()),
            route = json.route.validate(),
            destination = json.destination.validate(),
            direction = json.direction.validate(),
            origin = json.origin.validate()
        )
    }
}
