package io.rtpi.service.dublinbus

import com.google.common.cache.LoadingCache
import com.google.inject.Inject
import com.google.inject.name.Named
import io.rtpi.api.LiveData

class CachedDublinBusLiveDataService @Inject constructor(
    @Named("DUBLIN_BUS") private val dublinBusLiveDataCache: LoadingCache<String, List<LiveData>>
) {

    fun getLiveData(stopId: String): List<LiveData> = dublinBusLiveDataCache[stopId]
}
