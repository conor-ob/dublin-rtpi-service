package io.rtpi.service.rtpi

import io.reactivex.Single
import io.rtpi.api.Operator
import io.rtpi.api.Route
import io.rtpi.api.ServiceLocationRoutes
import io.rtpi.external.rtpi.RtpiApi
import io.rtpi.external.rtpi.RtpiBusStopInformationJson
import io.rtpi.external.rtpi.RtpiBusStopInformationResponseJson
import io.rtpi.util.RouteComparator
import io.rtpi.validation.validate
import io.rtpi.validation.validateDoubles
import io.rtpi.validation.validateStrings

private const val JSON = "json"

abstract class AbstractRtpiStopService<T : ServiceLocationRoutes>(
    private val rtpiApi: RtpiApi,
    private val operator: String
) {

    fun getStops(): Single<List<T>> {
        return rtpiApi.busStopInformation(operator = operator, format = JSON)
            .map { validateResponse(it) }
    }

    private fun validateResponse(response: RtpiBusStopInformationResponseJson): List<T> =
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

    protected abstract fun newServiceLocationInstance(json: RtpiBusStopInformationJson): T?

    protected fun mapOperators(json: RtpiBusStopInformationJson): Set<Operator> =
        if (json.operators.isNullOrEmpty()) {
            emptySet()
        } else {
            json.operators
                .validate()
                .map { operator ->
                    Operator.parse(operator.name.validate())
                }
                .toSet()
        }

    protected fun mapRoutes(json: RtpiBusStopInformationJson): List<Route> =
        if (json.operators.isNullOrEmpty()) {
            emptyList()
        } else {
            json.operators
                .validate()
                .flatMap { operator ->
                    if (operator.routes.isNullOrEmpty()) {
                        emptyList()
                    } else {
                        operator.routes
                            .validate()
                            .mapNotNull { routeId ->
                                val validatedRouteId = routeId.validate()
                                if (filterRoute(validatedRouteId)) {
                                    Route(
                                        id = validatedRouteId,
                                        operator = Operator.parse(operator.name.validate())
                                    )
                                } else {
                                    null
                                }
                            }
                    }
                }
                .toSet()
                .sortedWith(RouteComparator)
    }

    protected abstract fun filterRoute(routeId: String): Boolean
}
