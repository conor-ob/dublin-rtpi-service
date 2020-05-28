package io.rtpi.service.aircoach

import com.google.common.cache.LoadingCache
import com.google.inject.Inject
import com.google.inject.name.Named
import io.rtpi.api.LiveData

class CachedAircoachLiveDataService @Inject constructor(
    @Named("AIRCOACH") private val aircoachLiveDataCache: LoadingCache<String, List<LiveData>>
) {

    fun getLiveData(stopId: String): List<LiveData> = aircoachLiveDataCache[stopId]
}
