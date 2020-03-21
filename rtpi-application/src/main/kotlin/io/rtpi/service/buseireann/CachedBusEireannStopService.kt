package io.rtpi.service.buseireann

import com.google.common.cache.LoadingCache
import com.google.inject.Inject
import io.rtpi.api.BusEireannStop
import io.rtpi.api.Service

class CachedBusEireannStopService @Inject constructor(
    private val busEireannStopCache: LoadingCache<Service, List<BusEireannStop>>
) {

    fun getStops(): List<BusEireannStop> = busEireannStopCache[Service.BUS_EIREANN]
}
