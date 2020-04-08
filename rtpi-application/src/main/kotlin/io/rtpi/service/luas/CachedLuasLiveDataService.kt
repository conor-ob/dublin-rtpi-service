package io.rtpi.service.luas

import com.google.common.cache.LoadingCache
import com.google.inject.Inject
import com.google.inject.name.Named
import io.rtpi.api.LiveData

class CachedLuasLiveDataService @Inject constructor(
    @Named("LUAS") private val luasLiveDataCache: LoadingCache<String, List<LiveData>>
) {

    fun getLiveData(stopId: String): List<LiveData> = luasLiveDataCache[stopId]
}
