package io.rtpi.service.luas

import com.google.common.cache.LoadingCache
import com.google.inject.Inject
import io.rtpi.api.LuasLiveData

class CachedLuasLiveDataService @Inject constructor(
    private val luasLiveDataCache: LoadingCache<String, List<LuasLiveData>>
) {

    fun getLiveData(stopId: String): List<LuasLiveData> = luasLiveDataCache[stopId]
}
