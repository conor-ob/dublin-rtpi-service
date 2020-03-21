package io.rtpi.service.aircoach

import com.google.common.cache.LoadingCache
import com.google.inject.Inject
import io.rtpi.api.AircoachLiveData

class CachedAircoachLiveDataService @Inject constructor(
    private val aircoachLiveDataCache: LoadingCache<String, List<AircoachLiveData>>
) {

    fun getLiveData(stopId: String): List<AircoachLiveData> = aircoachLiveDataCache[stopId]
}
