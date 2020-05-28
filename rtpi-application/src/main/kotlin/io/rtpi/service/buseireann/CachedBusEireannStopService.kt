package io.rtpi.service.buseireann

import com.google.common.cache.LoadingCache
import com.google.inject.Inject
import com.google.inject.name.Named
import io.rtpi.api.Service
import io.rtpi.api.ServiceLocation

class CachedBusEireannStopService @Inject constructor(
    @Named("BUS_EIREANN") private val busEireannStopCache: LoadingCache<Service, List<ServiceLocation>>
) {

    fun getStops(): List<ServiceLocation> = busEireannStopCache[Service.BUS_EIREANN]
}
