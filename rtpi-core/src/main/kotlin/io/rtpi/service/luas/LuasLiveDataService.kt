package io.rtpi.service.luas

import com.google.inject.Inject
import io.rtpi.api.LuasLiveData
import io.rtpi.api.Operator
import io.rtpi.external.rtpi.RtpiApi
import io.rtpi.external.rtpi.RtpiRealTimeBusInformationJson
import io.rtpi.service.rtpi.AbstractRtpiLiveDataService
import io.rtpi.validation.validate

class LuasLiveDataService @Inject constructor(
    rtpiApi: RtpiApi
) : AbstractRtpiLiveDataService<LuasLiveData>(
    rtpiApi = rtpiApi,
    operator = Operator.LUAS.shortName
) {

    override fun newLiveDataInstance(timestamp: String, json: RtpiRealTimeBusInformationJson): LuasLiveData {
        return LuasLiveData(
            liveTime = createDueTime(timestamp, json),
            operator = Operator.parse(json.operator.validate()),
            route = json.route.validate(),
            destination = json.destination.validate().replace("LUAS", "").validate(),
            direction = json.direction.validate(),
            origin = json.origin.validate().replace("LUAS", "").validate()
        )
    }
}
