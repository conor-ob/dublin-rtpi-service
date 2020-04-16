package io.rtpi.service.dublinbus

import com.google.inject.Inject
import com.google.inject.name.Named
import io.reactivex.Single
import io.rtpi.api.ServiceLocation
import io.rtpi.external.dublinbus.DublinBusApi
import io.rtpi.external.rtpi.RtpiApi

class DublinBusStopService @Inject constructor(
    dublinBusApi: DublinBusApi,
    @Named("rtpi_api") rtpiApi: RtpiApi,
    @Named("rtpi_fallback_api") rtpiFallbackApi: RtpiApi
) {

    private val dublinBusDefaultStopService = DublinBusDefaultStopService(dublinBusApi)
    private val dublinBusRtpiStopService = DublinBusRtpiStopService(rtpiApi, rtpiFallbackApi)

    fun getStops(): Single<List<ServiceLocation>> {
        // TODO
        return dublinBusRtpiStopService.getStops()
//        return Single.zip(
//            dublinBusDefaultStopService.getStops(),
//            dublinBusRtpiStopService.getStops(),
//            BiFunction { defaultStops, rtpiStops -> resolve(defaultStops, rtpiStops) }
//        )
    }

    private fun resolve(defaultStops: List<ServiceLocation>, rtpiStops: List<ServiceLocation>): List<ServiceLocation> {
        val defaultStopsById = defaultStops.associateBy { it.id }.toMutableMap()
        val rtpiStopsById = rtpiStops.associateBy { it.id }
        for (key in rtpiStopsById.keys) {
            defaultStopsById.remove(key)
        }
        return rtpiStops.plus(defaultStopsById.values)
    }
}
