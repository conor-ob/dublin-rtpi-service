package io.rtpi.service.irishrail

import com.google.common.cache.LoadingCache
import com.google.inject.Inject
import io.rtpi.api.IrishRailLiveData

class CachedIrishRailLiveDataService @Inject constructor(
    private val irishRailLiveDataCache: LoadingCache<String, List<IrishRailLiveData>>
) {

    fun getLiveData(stationId: String): List<IrishRailLiveData> = irishRailLiveDataCache[stationId]
}
