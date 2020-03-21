package io.rtpi.service.dublinbus

import com.google.common.cache.LoadingCache
import com.google.inject.Inject
import io.rtpi.api.DublinBusLiveData

class CachedDublinBusLiveDataService @Inject constructor(
    private val dublinBusLiveDataCache: LoadingCache<String, List<DublinBusLiveData>>
) {

    fun getLiveData(stopId: String): List<DublinBusLiveData> = dublinBusLiveDataCache[stopId]
}
