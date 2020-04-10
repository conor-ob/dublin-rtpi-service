package io.rtpi.service.dublinbus

import com.google.inject.Inject
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import io.rtpi.api.Operator
import io.rtpi.api.ServiceLocation
import io.rtpi.api.StopLocation
import io.rtpi.external.rtpi.RtpiApi

class DublinBusRtpiStopService @Inject constructor(rtpiApi: RtpiApi) {

    private val dublinBusStopService = DublinBusRtpiInternalStopService(rtpiApi, Operator.DUBLIN_BUS.shortName)
    private val goAheadStopService = DublinBusRtpiInternalStopService(rtpiApi, Operator.GO_AHEAD.shortName)

    fun getStops(): Single<List<ServiceLocation>> {
        return Single.zip(
            dublinBusStopService.getStops(),
            goAheadStopService.getStops(),
            BiFunction { dublinBusStops, goAheadStops ->
                aggregate(dublinBusStops as List<StopLocation>, goAheadStops as List<StopLocation>)
            }
        )
    }

    private fun aggregate(
        dublinBusStops: List<StopLocation>,
        goAheadStops: List<StopLocation>
    ): List<ServiceLocation> {
        val aggregated = dublinBusStops.associateBy { it.id }.toMutableMap()
        for (goAheadStop in goAheadStops) {
            var existing = aggregated[goAheadStop.id]
            if (existing == null) {
                aggregated[goAheadStop.id] = goAheadStop
            } else {
                val existingRouteGroups = existing.routes.toMutableList()
                existingRouteGroups.addAll(goAheadStop.routes)
                existing = existing.copy(
                    routes = existingRouteGroups
                )
                aggregated[goAheadStop.id] = existing
            }
        }
        return aggregated.values.toList()
    }
}
