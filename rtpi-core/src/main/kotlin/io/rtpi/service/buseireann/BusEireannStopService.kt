package io.rtpi.service.buseireann

import com.google.inject.Inject
import io.rtpi.api.Coordinate
import io.rtpi.api.Operator
import io.rtpi.api.Service
import io.rtpi.api.StopLocation
import io.rtpi.external.rtpi.RtpiApi
import io.rtpi.external.rtpi.RtpiBusStopInformationJson
import io.rtpi.service.rtpi.AbstractRtpiStopService
import io.rtpi.validation.validate

class BusEireannStopService @Inject constructor(
    rtpiApi: RtpiApi
) : AbstractRtpiStopService(
    rtpiApi = rtpiApi,
    operator = Operator.BUS_EIREANN.shortName
) {

    override fun newServiceLocationInstance(json: RtpiBusStopInformationJson): StopLocation {
        return StopLocation(
            id = json.stopId.validate(),
            name = json.fullName.validate(),
            service = Service.BUS_EIREANN,
            coordinate = Coordinate(
                latitude = json.latitude.validate().toDouble(),
                longitude = json.longitude.validate().toDouble()
            ),
            routeGroups = mapRouteGroups(json),
            properties = mutableMapOf()
        )
    }

    override fun filterRoute(routeId: String): Boolean = true
}
