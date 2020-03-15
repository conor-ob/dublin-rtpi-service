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
                json.id != null
                    && json.name != null
                    && json.stopLatitude != null
                    && json.stopLongitude != null
                    && json.services != null
            }.map { json ->
                AircoachStop(
                    id = json.id!!.trim(),
                    name = json.name!!.trim(),
                    coordinate = Coordinate(
                        latitude = json.stopLatitude!!,
                        longitude = json.stopLongitude!!
                    ),
                    operators = setOf(Operator.AIRCOACH),
                    routes = json.services!!
                        .map { serviceJson ->
                            Route(
                                id = serviceJson.route!!.trim(),
                                operator = Operator.AIRCOACH
                            )
                        }.toSet().sortedWith(RouteComparator)
                )
            }
    }

    private fun retryFetchStaticDataAircoachStops(): Single<List<AircoachStop>> {
        return staticDataApi.getAircoachStops()
            .map { stopsJson ->
                stopsJson
                    .filter { json ->
                        json.id != null
                            && json.name != null
                            && json.stopLatitude != null
                            && json.stopLongitude != null
                            && json.services != null
                    }.map { json ->
                    AircoachStop(
                        id = json.id!!.trim(),
                        name = json.name!!.trim(),
                        coordinate = Coordinate(
                            latitude = json.stopLatitude!!,
                            longitude = json.stopLongitude!!
                        ),
                        operators = setOf(Operator.AIRCOACH),
                        routes = json.services!!
                            .map { serviceJson ->
                                Route(
                                    id = serviceJson.route!!.trim(),
                                    operator = Operator.AIRCOACH
                                )
                            }.toSet().sortedWith(RouteComparator)
                    )
                }
            }
    }

}
