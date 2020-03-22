package io.rtpi.service.luas

import com.google.common.cache.LoadingCache
import com.google.inject.Inject
import io.rtpi.api.LuasStop
import io.rtpi.api.Service

class CachedLuasStopService @Inject constructor(
    private val luasStopCache: LoadingCache<Service, List<LuasStop>>
) {

    fun getStops(): List<LuasStop> = luasStopCache[Service.LUAS]
}
