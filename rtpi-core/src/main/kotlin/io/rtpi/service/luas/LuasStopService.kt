package io.rtpi.service.luas

import com.google.inject.Inject
import io.rtpi.api.Coordinate
import io.rtpi.api.Operator
import io.rtpi.api.Service
import io.rtpi.api.StopLocation
import io.rtpi.external.rtpi.RtpiApi
import io.rtpi.external.rtpi.RtpiBusStopInformationJson
import io.rtpi.service.rtpi.AbstractRtpiStopService
import io.rtpi.validation.validate

class LuasStopService @Inject constructor(
    rtpiApi: RtpiApi
) : AbstractRtpiStopService(
    rtpiApi = rtpiApi,
    operator = Operator.LUAS.shortName
) {

    override fun newServiceLocationInstance(json: RtpiBusStopInformationJson): StopLocation {
        return StopLocation(
            id = json.stopId.validate(),
            name = json.fullName.validate().replace("LUAS", "").validate(),
            service = Service.LUAS,
            coordinate = Coordinate(
                latitude = json.latitude.validate().toDouble(),
                longitude = json.longitude.validate().toDouble()
            ),
            routeGroups = mapRouteGroups(json)
        )
    }

    override fun filterRoute(routeId: String): Boolean {
        if ("XXX".equals(routeId, ignoreCase = true) || "Luas".equals(routeId, ignoreCase = true)) {
            return false
        }
        return true
    }
}
