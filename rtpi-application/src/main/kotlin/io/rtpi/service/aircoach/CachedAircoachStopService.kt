package io.rtpi.service.aircoach

import com.google.common.cache.LoadingCache
import com.google.inject.Inject
import io.rtpi.api.AircoachStop
import io.rtpi.api.Service

class CachedAircoachStopService @Inject constructor(
    private val aircoachStopCache: LoadingCache<Service, List<AircoachStop>>
) {

    fun getStops(): List<AircoachStop> = aircoachStopCache[Service.AIRCOACH]
}
