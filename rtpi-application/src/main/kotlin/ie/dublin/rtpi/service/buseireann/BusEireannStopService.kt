package ie.dublin.rtpi.service.buseireann

import ie.dublin.rtpi.api.BusEireannStop
import ie.dublin.rtpi.api.Coordinate
import ie.dublin.rtpi.api.Operator
import ie.dublin.rtpi.ktx.validate
import ie.dublin.rtpi.resource.rtpi.RtpiApi

class BusEireannStopService(private val rtpiService: RtpiApi) {

    fun getStops(): List<BusEireannStop> {
        return rtpiService.busStopInformation(operator = "be", format = "json")
            .validate()
            .results
            .map { json ->
                BusEireannStop(
                        id = json.stopId,
                        name = json.fullName!!,
                        coordinate = Coordinate(json.latitude.toDouble(), json.longitude.toDouble()),
                        operators = json.operators.map { operator -> Operator.parse(operator.name) }.toSet(),
                        routes = json.operators.associateBy( { Operator.parse(it.name) }, { it.routes } )
                    )
            }
    }

}
