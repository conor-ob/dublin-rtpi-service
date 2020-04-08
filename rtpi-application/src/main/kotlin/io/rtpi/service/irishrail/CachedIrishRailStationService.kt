package io.rtpi.service.irishrail

import com.google.common.cache.LoadingCache
import com.google.inject.Inject
import com.google.inject.name.Named
import io.rtpi.api.Service
import io.rtpi.api.ServiceLocation

class CachedIrishRailStationService @Inject constructor(
    @Named("IRISH_RAIL") private val irishRailStationCache: LoadingCache<Service, List<ServiceLocation>>
) {

    fun getStations(): List<ServiceLocation> = irishRailStationCache[Service.IRISH_RAIL]
}

