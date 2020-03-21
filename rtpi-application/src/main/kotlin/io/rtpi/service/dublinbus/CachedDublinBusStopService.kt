package io.rtpi.service.dublinbus

import com.google.common.cache.LoadingCache
import com.google.inject.Inject
import io.rtpi.api.DublinBusStop
import io.rtpi.api.Service

class CachedDublinBusStopService @Inject constructor(
    private val dublinBusStopCache: LoadingCache<Service, List<DublinBusStop>>
) {

    fun getStops(): List<DublinBusStop> = dublinBusStopCache[Service.DUBLIN_BUS]
}
