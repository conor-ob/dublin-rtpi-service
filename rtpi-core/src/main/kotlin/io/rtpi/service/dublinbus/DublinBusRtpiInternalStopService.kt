package io.rtpi.service.dublinbus

import com.google.inject.name.Named
import io.rtpi.api.Coordinate
import io.rtpi.api.Service
import io.rtpi.api.StopLocation
import io.rtpi.external.rtpi.RtpiApi
import io.rtpi.external.rtpi.RtpiBusStopInformationJson
import io.rtpi.service.rtpi.AbstractRtpiStopService
import io.rtpi.validation.validate

class DublinBusRtpiInternalStopService(
    @Named("rtpi_api") rtpiApi: RtpiApi,
    @Named("rtpi_fallback_api") rtpiFallbackApi: RtpiApi,
    operator: String
) : AbstractRtpiStopService(
    rtpiApi = rtpiApi,
    rtpiFallbackApi = rtpiFallbackApi,
    operator = operator
) {

    override fun newServiceLocationInstance(json: RtpiBusStopInformationJson): StopLocation? {
        return StopLocation(
            id = json.stopId.validate(),
            name = json.fullName.validate(),
            service = Service.DUBLIN_BUS,
            coordinate = Coordinate(
                latitude = json.latitude.validate().toDouble(),
                longitude = json.longitude.validate().toDouble()
            ),
            routeGroups = mapRouteGroups(json)
        )
    }

    override fun filterRoute(routeId: String): Boolean = true
}
