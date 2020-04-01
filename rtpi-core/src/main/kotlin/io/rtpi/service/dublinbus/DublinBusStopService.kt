package io.rtpi.service.dublinbus

import com.google.inject.Inject
import io.reactivex.Single
import io.rtpi.api.DublinBusStop
import io.rtpi.external.dublinbus.DublinBusApi
import io.rtpi.external.rtpi.RtpiApi

class DublinBusStopService @Inject constructor(
    dublinBusApi: DublinBusApi,
    rtpiApi: RtpiApi
) {

    private val dublinBusDefaultStopService = DublinBusDefaultStopService(dublinBusApi)
    private val dublinBusRtpiStopService = DublinBusRtpiStopService(rtpiApi)

    fun getStops(): Single<List<DublinBusStop>> {
        // TODO
        return dublinBusRtpiStopService.getStops()
//        return Single.zip(
//            dublinBusDefaultStopService.getStops(),
//            dublinBusRtpiStopService.getStops(),
//            BiFunction { defaultStops, rtpiStops -> resolve(defaultStops, rtpiStops) }
//        )
    }

    private fun resolve(defaultStops: List<DublinBusStop>, rtpiStops: List<DublinBusStop>): List<DublinBusStop> {
        val defaultStopsById = defaultStops.associateBy { it.id }.toMutableMap()
        val rtpiStopsById = rtpiStops.associateBy { it.id }
        for (key in rtpiStopsById.keys) {
            defaultStopsById.remove(key)
        }
        return rtpiStops.plus(defaultStopsById.values)
    }
}
