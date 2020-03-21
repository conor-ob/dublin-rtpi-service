package io.rtpi.service.irishrail

import com.google.common.cache.LoadingCache
import com.google.inject.Inject
import io.rtpi.api.IrishRailStation
import io.rtpi.api.Service

class CachedIrishRailStationService @Inject constructor(
    private val irishRailStationCache: LoadingCache<Service, List<IrishRailStation>>
) {

    fun getStations(): List<IrishRailStation> = irishRailStationCache[Service.IRISH_RAIL]
}

