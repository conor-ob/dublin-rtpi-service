package io.rtpi.service.aircoach

import com.google.inject.Inject
import io.reactivex.Single
import io.rtpi.api.AircoachStop
import io.rtpi.api.Coordinate
import io.rtpi.api.Operator
import io.rtpi.api.Route
import io.rtpi.external.aircoach.AircoachWebScraper
import io.rtpi.external.staticdata.StaticDataApi
import io.rtpi.util.RouteComparator
import io.rtpi.validation.validate
import io.rtpi.validation.validateCollection
import io.rtpi.validation.validateDoubles
import io.rtpi.validation.validateStrings
import java.lang.Exception

class AircoachStopService @Inject constructor(
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
            .filter { json ->
                validateStrings(json.id, json.name) &&
                    validateDoubles(json.stopLatitude, json.stopLongitude) &&
                    validateCollection(json.services)
            }
            .map { json ->
                AircoachStop(
                    id = json.id.validate(),
                    name = json.name.validate(),
                    coordinate = Coordinate(
                        latitude = json.stopLatitude.validate(),
                        longitude = json.stopLongitude.validate()
                    ),
                    operators = setOf(Operator.AIRCOACH),
                    routes = json.services.validate()
                        .mapNotNull { serviceJson ->
                            if (serviceJson.route == null) {
                                null
                            } else {
                                Route(
                                    id = serviceJson.route.validate(),
                                    operator = Operator.AIRCOACH
                                )
                            }
                        }.toSet().sortedWith(RouteComparator)
                )
            }
    }

    private fun retryFetchStaticDataAircoachStops(): Single<List<AircoachStop>> {
        return staticDataApi.getAircoachStops()
            .map { stopsJson ->
                stopsJson
                    .filter { json ->
                        validateStrings(json.id, json.name) &&
                            validateDoubles(json.stopLatitude, json.stopLongitude) &&
                            validateCollection(json.services)
                    }
                    .map { json ->
                        AircoachStop(
                            id = json.id.validate(),
                            name = json.name.validate(),
                            coordinate = Coordinate(
                                latitude = json.stopLatitude.validate(),
                                longitude = json.stopLongitude.validate()
                            ),
                            operators = setOf(Operator.AIRCOACH),
                            routes = json.services.validate()
                                .mapNotNull { serviceJson ->
                                    if (serviceJson.route == null) {
                                        null
                                    } else {
                                        Route(
                                            id = serviceJson.route.validate(),
                                            operator = Operator.AIRCOACH
                                        )
                                    }
                                }.toSet().sortedWith(RouteComparator)
                        )
                }
            }
    }

}
