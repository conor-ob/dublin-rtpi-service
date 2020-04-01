package io.rtpi.service.dublinbus

import com.google.inject.Inject
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import io.rtpi.api.DublinBusStop
import io.rtpi.api.Operator
import io.rtpi.external.rtpi.RtpiApi
import io.rtpi.util.RouteComparator

class DublinBusRtpiStopService @Inject constructor(rtpiApi: RtpiApi) {

    private val dublinBusStopService = DublinBusRtpiInternalStopService(rtpiApi, Operator.DUBLIN_BUS.shortName)
    private val goAheadStopService = DublinBusRtpiInternalStopService(rtpiApi, Operator.GO_AHEAD.shortName)

    fun getStops(): Single<List<DublinBusStop>> {
        return Single.zip(
            dublinBusStopService.getStops(),
            goAheadStopService.getStops(),
            BiFunction { dublinBusStops, goAheadStops ->
                aggregate(dublinBusStops, goAheadStops)
            }
        )
    }

    private fun aggregate(
        dublinBusStops: List<DublinBusStop>,
        goAheadStops: List<DublinBusStop>
    ): List<DublinBusStop> {
        val aggregated = dublinBusStops.associateBy { it.id }.toMutableMap()
        for (goAheadStop in goAheadStops) {
            var existing = aggregated[goAheadStop.id]
            if (existing == null) {
                aggregated[goAheadStop.id] = goAheadStop
            } else {
                val existingOperators = existing.operators.toMutableSet()
                existingOperators.addAll(goAheadStop.operators)
                val existingRoutes = existing.routes.toMutableList()
                existingRoutes.addAll(goAheadStop.routes)
                existing = existing.copy(
                    operators = existingOperators,
                    routes = existingRoutes.sortedWith(RouteComparator)
                )
                aggregated[goAheadStop.id] = existing
            }
        }
        return aggregated.values.toList()
    }
}
