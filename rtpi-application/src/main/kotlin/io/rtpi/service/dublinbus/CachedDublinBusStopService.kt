package io.rtpi.service.dublinbus

import com.google.common.cache.LoadingCache
import com.google.inject.Inject
import com.google.inject.name.Named
import io.rtpi.api.Service
import io.rtpi.api.ServiceLocation

class CachedDublinBusStopService @Inject constructor(
    @Named("DUBLIN_BUS") private val dublinBusStopCache: LoadingCache<Service, List<ServiceLocation>>
) {

    fun getStops(): List<ServiceLocation> = dublinBusStopCache[Service.DUBLIN_BUS]
}
