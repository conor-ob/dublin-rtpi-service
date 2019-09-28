package ie.dublin.rtpi.service.luas

import ie.dublin.rtpi.api.Coordinate
import ie.dublin.rtpi.api.LuasStop
import ie.dublin.rtpi.api.Operator
import ie.dublin.rtpi.ktx.validate
import ie.dublin.rtpi.resource.rtpi.RtpiApi

class LuasStopService(private val rtpiService: RtpiApi) {

    fun getStops(): List<LuasStop> {
        return rtpiService.busStopInformation(operator = "luas", format = "json")
            .validate()
            .results
            .map { json ->
                LuasStop(
                    id = json.stopId,
                    name = json.fullName!!.replace("LUAS ", ""),
                    coordinate = Coordinate(json.latitude.toDouble(), json.longitude.toDouble()),
                    operators = json.operators.map { operator -> Operator.parse(operator.name) }.toSet(),
                    routes = json.operators.associateBy( { Operator.parse(it.name) }, { it.routes } )
                )
            }
    }

}
