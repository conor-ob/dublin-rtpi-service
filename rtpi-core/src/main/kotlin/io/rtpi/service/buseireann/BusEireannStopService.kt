package io.rtpi.service.buseireann

import com.google.inject.Inject
import io.rtpi.api.BusEireannStop
import io.rtpi.api.Coordinate
import io.rtpi.api.Operator
import io.rtpi.api.Route
import io.rtpi.external.rtpi.RtpiApi
import io.rtpi.external.rtpi.RtpiBusStopInformationJson
import io.rtpi.service.rtpi.AbstractRtpiStopService
import io.rtpi.util.RouteComparator

class BusEireannStopService @Inject constructor(
    rtpiApi: RtpiApi
) : AbstractRtpiStopService<BusEireannStop>(
    rtpiApi = rtpiApi,
    operator = Operator.BUS_EIREANN.shortName
) {

    override fun newServiceLocationInstance(timestamp: String, json: RtpiBusStopInformationJson): BusEireannStop {
        return BusEireannStop(
            id = json.stopId!!.trim(),
            name = json.fullName!!.trim(),
            coordinate = Coordinate(
                latitude = json.latitude!!.toDouble(),
                longitude = json.longitude!!.toDouble()
            ),
            operators = json.operators.map { operator ->
                Operator.parse(operator.name!!.trim())
            }.toSet(),
            routes = json.operators.flatMap { operator ->
                operator.routes.map { routeId ->
                    Route(
                        id = routeId.trim(),
                        operator = Operator.parse(operator.name!!.trim())
                    )
                }
            }.toSet().sortedWith(RouteComparator)
        )
    }

}
