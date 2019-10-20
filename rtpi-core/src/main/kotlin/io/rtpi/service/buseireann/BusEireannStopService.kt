package io.rtpi.service.buseireann

import io.rtpi.api.BusEireannStop
import io.rtpi.api.Coordinate
import io.rtpi.api.Operator
import io.rtpi.ktx.validate
import io.rtpi.resource.rtpi.RtpiApi

class BusEireannStopService(private val rtpiApi: RtpiApi) {

    fun getStops(): List<BusEireannStop> {
        return rtpiApi.busStopInformation(operator = "be", format = "json")
            .validate()
            .results
            .filter { json ->
                json.stopId != null
                    && json.fullName != null
                    && json.latitude != null
                    && json.longitude != null
            }.map { json ->
                BusEireannStop(
                        id = json.stopId!!,
                        name = json.fullName!!,
                        coordinate = Coordinate(json.latitude!!.toDouble(), json.longitude!!.toDouble()),
                        operators = json.operators.map { operator -> Operator.parse(operator.name!!) }.toSet(),
                        routes = json.operators.associateBy( { Operator.parse(it.name!!) }, { it.routes } )
                    )
            }
    }

}
