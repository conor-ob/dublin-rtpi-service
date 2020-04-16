package io.rtpi.service.luas

import com.google.inject.Inject
import com.google.inject.name.Named
import io.rtpi.api.Coordinate
import io.rtpi.api.Operator
import io.rtpi.api.Service
import io.rtpi.api.StopLocation
import io.rtpi.external.rtpi.RtpiApi
import io.rtpi.external.rtpi.RtpiBusStopInformationJson
import io.rtpi.service.rtpi.AbstractRtpiStopService
import io.rtpi.validation.validate

class LuasStopService @Inject constructor(
    @Named("rtpi_api") rtpiApi: RtpiApi,
    @Named("rtpi_fallback_api") rtpiFallbackApi: RtpiApi
) : AbstractRtpiStopService(
    rtpiApi = rtpiApi,
    rtpiFallbackApi = rtpiFallbackApi,
    operator = Operator.LUAS.shortName
) {

    override fun newServiceLocationInstance(json: RtpiBusStopInformationJson): StopLocation? {
        return if (invalidStop(json)) {
            null
        } else {
            StopLocation(
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
    }

    private fun invalidStop(json: RtpiBusStopInformationJson): Boolean {
        val routes = json.operators?.flatMap {
            if (it.routes == null) {
                emptyList()
            } else {
                requireNotNull(it.routes).toList()
            }
        }
        return routes != null && routes.size == 1 && "XXX".equals(routes.first(), ignoreCase = true)
    }

    override fun filterRoute(routeId: String): Boolean {
        if ("XXX".equals(routeId, ignoreCase = true) || "Luas".equals(routeId, ignoreCase = true)) {
            return false
        }
        return true
    }
}
