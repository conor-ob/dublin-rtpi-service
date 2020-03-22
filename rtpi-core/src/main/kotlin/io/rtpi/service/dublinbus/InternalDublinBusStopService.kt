package io.rtpi.service.dublinbus

import io.rtpi.api.Coordinate
import io.rtpi.api.DublinBusStop
import io.rtpi.external.rtpi.RtpiApi
import io.rtpi.external.rtpi.RtpiBusStopInformationJson
import io.rtpi.service.rtpi.AbstractRtpiStopService
import io.rtpi.validation.validate

class InternalDublinBusStopService(
    rtpiApi: RtpiApi,
    operator: String
) : AbstractRtpiStopService<DublinBusStop>(
    rtpiApi = rtpiApi,
    operator = operator
) {

    override fun newServiceLocationInstance(json: RtpiBusStopInformationJson): DublinBusStop? {
        return DublinBusStop(
            id = json.stopId.validate(),
            name = json.fullName.validate(),
            coordinate = Coordinate(
                latitude = json.latitude.validate().toDouble(),
                longitude = json.longitude.validate().toDouble()
            ),
            operators = mapOperators(json),
            routes = mapRoutes(json)
        )
    }

    override fun filterRoute(routeId: String): Boolean = true
}
