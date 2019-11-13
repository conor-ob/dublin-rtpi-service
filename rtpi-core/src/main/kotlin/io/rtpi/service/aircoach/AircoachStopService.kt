package io.rtpi.service.aircoach

import io.rtpi.api.AircoachStop
import io.rtpi.api.Coordinate
import io.rtpi.api.Operator
import io.rtpi.api.Route
import io.rtpi.resource.aircoach.AircoachWebScraper

class AircoachStopService(private val aircoachWebScraper: AircoachWebScraper) {

    fun getStops(): List<AircoachStop> {
        return aircoachWebScraper.scrapeStops()
            .map { json ->
                AircoachStop(
                    id = json.id,
                    name = json.name,
                    coordinate = Coordinate(json.stopLatitude, json.stopLongitude),
                    operators = setOf(Operator.AIRCOACH),
                    routes = json.services.map { Route(it.route, Operator.AIRCOACH) }
                )
            }
    }

}
