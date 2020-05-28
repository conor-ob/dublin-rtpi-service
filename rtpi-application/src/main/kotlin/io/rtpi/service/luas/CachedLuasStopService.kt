package io.rtpi.service.luas

import com.google.common.cache.LoadingCache
import com.google.inject.Inject
import com.google.inject.name.Named
import io.rtpi.api.Service
import io.rtpi.api.ServiceLocation

class CachedLuasStopService @Inject constructor(
    @Named("LUAS") private val luasStopCache: LoadingCache<Service, List<ServiceLocation>>
) {

    fun getStops(): List<ServiceLocation> = luasStopCache[Service.LUAS]
}
