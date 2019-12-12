package io.rtpi.service.luas

import io.reactivex.Single
import io.rtpi.api.Coordinate
import io.rtpi.api.LuasStop
import io.rtpi.api.Operator
import io.rtpi.api.Route
import io.rtpi.external.rtpi.RtpiApi

class LuasStopService(private val rtpiApi: RtpiApi) {

    fun getStops(): Single<List<LuasStop>> {
        return rtpiApi.busStopInformation(operator = "luas", format = "json")
            .map { response ->
                response.results
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
                            routes = json.operators.flatMap { operator ->
                                operator.routes.map { Route(it.trim(), Operator.parse(operator.name!!)) }
                            }
                        )
                    }
            }
    }

}
