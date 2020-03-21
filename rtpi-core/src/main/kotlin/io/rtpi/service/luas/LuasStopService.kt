package io.rtpi.service.luas

import com.google.inject.Inject
import io.rtpi.api.Coordinate
import io.rtpi.api.LuasStop
import io.rtpi.api.Operator
import io.rtpi.external.rtpi.RtpiApi
import io.rtpi.external.rtpi.RtpiBusStopInformationJson
import io.rtpi.service.rtpi.AbstractRtpiStopService
import io.rtpi.validation.validate

class LuasStopService @Inject constructor(
    rtpiApi: RtpiApi
) : AbstractRtpiStopService<LuasStop>(
    rtpiApi = rtpiApi,
    operator = Operator.LUAS.shortName
) {

    override fun newServiceLocationInstance(timestamp: String, json: RtpiBusStopInformationJson): LuasStop? {
        return LuasStop(
            id = json.stopId.validate(),
            name = json.fullName.validate().replace("LUAS", "").validate(),
            coordinate = Coordinate(
                latitude = json.latitude.validate().toDouble(),
                longitude = json.longitude.validate().toDouble()
            ),
            operators = mapOperators(json),
            routes = mapRoutes(json)
        )
    }

    override fun filterRoute(routeId: String): Boolean {
        if ("XXX".equals(routeId, ignoreCase = true) || "Luas".equals(routeId, ignoreCase = true)) {
            return false
        }
        return true
    }
}
