package io.rtpi.service.buseireann

import com.google.inject.Inject
import io.rtpi.api.BusEireannStop
import io.rtpi.api.Coordinate
import io.rtpi.api.Operator
import io.rtpi.external.rtpi.RtpiApi
import io.rtpi.external.rtpi.RtpiBusStopInformationJson
import io.rtpi.service.rtpi.AbstractRtpiStopService
import io.rtpi.validation.validate

class BusEireannStopService @Inject constructor(
    rtpiApi: RtpiApi
) : AbstractRtpiStopService<BusEireannStop>(
    rtpiApi = rtpiApi,
    operator = Operator.BUS_EIREANN.shortName
) {

    override fun newServiceLocationInstance(timestamp: String, json: RtpiBusStopInformationJson): BusEireannStop {
        return BusEireannStop(
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
