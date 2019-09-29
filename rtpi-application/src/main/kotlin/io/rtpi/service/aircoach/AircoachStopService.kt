package io.rtpi.service.aircoach

import io.rtpi.api.AircoachStop
import io.rtpi.api.Coordinate
import io.rtpi.api.Operator
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
                    routes = mapOf(Operator.AIRCOACH to json.services.map { it.route })
                )
            }
    }

}
