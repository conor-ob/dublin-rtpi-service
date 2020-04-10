package io.rtpi.service.aircoach

import com.google.inject.Inject
import io.reactivex.Single
import io.rtpi.api.Coordinate
import io.rtpi.api.Operator
import io.rtpi.api.RouteGroup
import io.rtpi.api.Service
import io.rtpi.api.ServiceLocation
import io.rtpi.api.StopLocation
import io.rtpi.external.aircoach.AircoachStopJson
import io.rtpi.external.aircoach.AircoachWebScraper
import io.rtpi.external.staticdata.StaticDataApi
import io.rtpi.util.AlphaNumericComparator
import io.rtpi.validation.validate
import io.rtpi.validation.validateCollection
import io.rtpi.validation.validateDoubles
import io.rtpi.validation.validateStrings
import java.lang.Exception

class AircoachStopService @Inject constructor(
    private val aircoachWebScraper: AircoachWebScraper,
    private val staticDataApi: StaticDataApi
) {

    fun getStops(): Single<List<ServiceLocation>> {
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

    private fun scrapeAircoachStops(): List<ServiceLocation> {
        return aircoachWebScraper.scrapeStops()
            .filter { json ->
                validateStrings(json.id, json.name) &&
                    validateDoubles(json.stopLatitude, json.stopLongitude) &&
                    validateCollection(json.services)
            }
            .map { json ->
                StopLocation(
                    id = json.id.validate(),
                    name = json.name.validate(),
                    service = Service.AIRCOACH,
                    coordinate = Coordinate(
                        latitude = json.stopLatitude.validate(),
                        longitude = json.stopLongitude.validate()
                    ),
                    routes = mapRouteGroups(json)
                )
            }
    }

    private fun retryFetchStaticDataAircoachStops(): Single<List<ServiceLocation>> {
        return staticDataApi.getAircoachStops()
            .map { stopsJson ->
                stopsJson
                    .filter { json ->
                        validateStrings(json.id, json.name) &&
                            validateDoubles(json.stopLatitude, json.stopLongitude) &&
                            validateCollection(json.services)
                    }
                    .map { json ->
                        StopLocation(
                            id = json.id.validate(),
                            name = json.name.validate(),
                            service = Service.AIRCOACH,
                            coordinate = Coordinate(
                                latitude = json.stopLatitude.validate(),
                                longitude = json.stopLongitude.validate()
                            ),
                            routes = mapRouteGroups(json)
                        )
                }
            }
    }

    private fun mapRouteGroups(json: AircoachStopJson): List<RouteGroup> {
        return listOf(
            RouteGroup(
                operator = Operator.AIRCOACH,
                routes = json.services.validate()
                    .mapNotNull { serviceJson ->
                        if (serviceJson.route == null) {
                            null
                        } else {
                            serviceJson.route.validate()
                        }
                    }.toSet().sortedWith(AlphaNumericComparator)
            )
        )
    }
}
