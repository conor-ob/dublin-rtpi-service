package io.rtpi.service.buseireann

import com.google.common.cache.LoadingCache
import com.google.inject.Inject
import io.rtpi.api.BusEireannLiveData

class CachedBusEireannLiveDataService @Inject constructor(
    private val busEireannLiveDataCache: LoadingCache<String, List<BusEireannLiveData>>
) {

    fun getLiveData(stopId: String): List<BusEireannLiveData> = busEireannLiveDataCache[stopId]
}
