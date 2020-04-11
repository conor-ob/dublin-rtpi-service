package io.rtpi.service.buseireann

import com.google.common.cache.LoadingCache
import com.google.inject.Inject
import com.google.inject.name.Named
import io.rtpi.api.LiveData

class CachedBusEireannLiveDataService @Inject constructor(
    @Named("BUS_EIREANN") private val busEireannLiveDataCache: LoadingCache<String, List<LiveData>>
) {

    fun getLiveData(stopId: String): List<LiveData> = busEireannLiveDataCache[stopId]
}
