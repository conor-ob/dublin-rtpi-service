package io.rtpi.service.luas

import com.google.inject.Inject
import io.rtpi.api.Coordinate
import io.rtpi.api.LuasStop
import io.rtpi.api.Operator
import io.rtpi.api.Route
import io.rtpi.external.rtpi.RtpiApi
import io.rtpi.external.rtpi.RtpiBusStopInformationJson
import io.rtpi.service.rtpi.AbstractRtpiStopService
import io.rtpi.util.RouteComparator

class LuasStopService @Inject constructor(
    rtpiApi: RtpiApi
) : AbstractRtpiStopService<LuasStop>(
    rtpiApi = rtpiApi,
    operator = Operator.LUAS.shortName
) {

    override fun newServiceLocationInstance(timestamp: String, json: RtpiBusStopInformationJson): LuasStop {
        return LuasStop(
            id = json.stopId!!.trim(),
            name = json.fullName!!.trim().replace("LUAS ", ""),
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
