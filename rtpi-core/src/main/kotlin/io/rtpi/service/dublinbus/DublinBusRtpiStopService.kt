package io.rtpi.service.dublinbus

import com.google.inject.Inject
import com.google.inject.name.Named
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import io.rtpi.api.Operator
import io.rtpi.api.ServiceLocation
import io.rtpi.api.StopLocation
import io.rtpi.external.rtpi.RtpiApi

class DublinBusRtpiStopService @Inject constructor(
    @Named("rtpi_api") rtpiApi: RtpiApi,
    @Named("rtpi_fallback_api") rtpiFallbackApi: RtpiApi
) {

    private val dublinBusStopService = DublinBusRtpiInternalStopService(rtpiApi, rtpiFallbackApi, Operator.DUBLIN_BUS.shortName)
    private val goAheadStopService = DublinBusRtpiInternalStopService(rtpiApi, rtpiFallbackApi, Operator.GO_AHEAD.shortName)

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
                val existingRouteGroups = existing.routeGroups.toMutableList()
                existingRouteGroups.addAll(goAheadStop.routeGroups)
                existing = existing.copy(
                    routeGroups = existingRouteGroups
                )
                aggregated[goAheadStop.id] = existing
            }
        }
        return aggregated.values.toList()
    }
}
