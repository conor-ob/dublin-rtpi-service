package io.rtpi.service.aircoach

import com.google.common.cache.LoadingCache
import com.google.inject.Inject
import com.google.inject.name.Named
import io.rtpi.api.Service
import io.rtpi.api.ServiceLocation

class CachedAircoachStopService @Inject constructor(
    @Named("AIRCOACH") private val aircoachStopCache: LoadingCache<Service, List<ServiceLocation>>
) {

    fun getStops(): List<ServiceLocation> = aircoachStopCache[Service.AIRCOACH]
}
