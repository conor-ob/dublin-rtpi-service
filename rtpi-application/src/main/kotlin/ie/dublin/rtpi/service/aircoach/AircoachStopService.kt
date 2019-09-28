package ie.dublin.rtpi.service.aircoach

import ie.dublin.rtpi.api.AircoachStop
import ie.dublin.rtpi.api.Coordinate
import ie.dublin.rtpi.api.Operator
import ie.dublin.rtpi.resource.aircoach.AircoachWebScraper

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
