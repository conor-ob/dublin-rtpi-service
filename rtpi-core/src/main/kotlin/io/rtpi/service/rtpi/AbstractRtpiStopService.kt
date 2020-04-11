package io.rtpi.service.rtpi

import io.reactivex.Single
import io.rtpi.api.Operator
import io.rtpi.api.RouteGroup
import io.rtpi.api.ServiceLocation
import io.rtpi.api.StopLocation
import io.rtpi.external.rtpi.RtpiApi
import io.rtpi.external.rtpi.RtpiBusStopInformationJson
import io.rtpi.external.rtpi.RtpiBusStopInformationResponseJson
import io.rtpi.util.AlphaNumericComparator
import io.rtpi.validation.validate
import io.rtpi.validation.validateDoubles
import io.rtpi.validation.validateStrings

private const val JSON = "json"

abstract class AbstractRtpiStopService(
    private val rtpiApi: RtpiApi,
    private val operator: String
) {

    fun getStops(): Single<List<ServiceLocation>> {
        return rtpiApi.busStopInformation(operator = operator, format = JSON)
            .map { validateResponse(it) }
    }

    private fun validateResponse(response: RtpiBusStopInformationResponseJson): List<StopLocation> =
        if (response.results.isNullOrEmpty()) {
            emptyList()
        } else {
            requireNotNull(response.results)
                .filter { json ->
                    validateStrings(json.stopId, json.fullName) &&
                        validateStrings(json.latitude, json.longitude) &&
                        validateDoubles(json.latitude?.toDouble(), json.longitude?.toDouble())
                }.mapNotNull { json -> newServiceLocationInstance(json) }
        }

    protected abstract fun newServiceLocationInstance(json: RtpiBusStopInformationJson): StopLocation?

    protected fun mapRouteGroups(json: RtpiBusStopInformationJson): List<RouteGroup> =
        if (json.operators.isNullOrEmpty()) {
            emptyList()
        } else {
            json.operators
                .validate()
                .map { operator ->
                    if (operator.routes.isNullOrEmpty()) {
                        RouteGroup(
                            operator = Operator.parse(operator.name.validate()),
                            routes = emptyList()
                        )
                    } else {
                        RouteGroup(
                            operator = Operator.parse(operator.name.validate()),
                            routes = operator.routes
                                .validate()
                                .mapNotNull { routeId ->
                                    val validatedRouteId = routeId.validate()
                                    if (filterRoute(validatedRouteId)) {
                                        validatedRouteId
                                    } else {
                                        null
                                    }
                                }
                                .toSet()
                                .sortedWith(AlphaNumericComparator)
                        )
                    }
                }
        }

    protected abstract fun filterRoute(routeId: String): Boolean
}
