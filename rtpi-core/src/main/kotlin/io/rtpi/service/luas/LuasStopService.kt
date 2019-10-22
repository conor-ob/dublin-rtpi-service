package io.rtpi.service.luas

import io.rtpi.api.Coordinate
import io.rtpi.api.LuasStop
import io.rtpi.api.Operator
import io.rtpi.ktx.validate
import io.rtpi.resource.rtpi.RtpiApi

class LuasStopService(private val rtpiApi: RtpiApi) {

    fun getStops(): List<LuasStop> {
        return rtpiApi.busStopInformation(operator = "luas", format = "json")
            .validate()
            .results
            .filter { json ->
                json.stopId != null
                    && json.fullName != null
                    && json.latitude != null
                    && json.longitude != null
            }.map { json ->
                LuasStop(
                    id = json.stopId!!.trim(),
                    name = json.fullName!!.trim().replace("LUAS ", ""),
                    coordinate = Coordinate(json.latitude!!.toDouble(), json.longitude!!.toDouble()),
                    operators = json.operators.map { operator -> Operator.parse(operator.name!!.trim()) }.toSet(),
                    routes = json.operators.associateBy( { Operator.parse(it.name!!.trim()) }, { it.routes } )
                )
            }
    }

}
