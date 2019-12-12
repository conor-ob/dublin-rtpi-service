package io.rtpi.service.aircoach

import io.reactivex.Single
import io.rtpi.api.AircoachStop
import io.rtpi.api.Coordinate
import io.rtpi.api.Operator
import io.rtpi.api.Route
import io.rtpi.external.aircoach.AircoachWebScraper
import io.rtpi.external.staticdata.StaticDataApi
import java.lang.Exception

class AircoachStopService(
    private val aircoachWebScraper: AircoachWebScraper,
    private val staticDataApi: StaticDataApi
) {

    fun getStops(): Single<List<AircoachStop>> {
        try {
            val stops = scrapeAircoachStops()
            if (stops.isEmpty()) {
                return retryFetchStaticDataAircoachStops()
            }
            return Single.just(stops)
        } catch (e: Exception) {
            return retryFetchStaticDataAircoachStops()
        }
    }

    private fun scrapeAircoachStops(): List<AircoachStop> {
        return aircoachWebScraper.scrapeStops()
            .map { json ->
                AircoachStop(
                    id = json.id,
                    name = json.name,
                    coordinate = Coordinate(json.stopLatitude, json.stopLongitude),
                    operators = setOf(Operator.AIRCOACH),
                    routes = json.services.map { Route(it.route, Operator.AIRCOACH) }.toSet().toList()
                )
            }
    }

    private fun retryFetchStaticDataAircoachStops(): Single<List<AircoachStop>> {
        return staticDataApi.getAircoachStops()
            .map { stopsJson ->
                stopsJson.map { json ->
                    AircoachStop(
                        id = json.id,
                        name = json.name,
                        coordinate = Coordinate(json.stopLatitude, json.stopLongitude),
                        operators = setOf(Operator.AIRCOACH),
                        routes = json.services.map { Route(it.route, Operator.AIRCOACH) }.toSet().toList()
                    )
                }
            }
    }

}
