package io.rtpi.service.irishrail

import com.google.common.cache.LoadingCache
import com.google.inject.Inject
import com.google.inject.name.Named
import io.rtpi.api.LiveData

class CachedIrishRailLiveDataService @Inject constructor(
    @Named("IRISH_RAIL") private val irishRailLiveDataCache: LoadingCache<String, List<LiveData>>
) {

    fun getLiveData(stationId: String): List<LiveData> = irishRailLiveDataCache[stationId]
}
