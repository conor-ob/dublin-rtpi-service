package io.rtpi.service.dublinbikes

import com.google.common.cache.LoadingCache
import com.google.inject.Inject
import io.rtpi.api.DublinBikesLiveData

class CachedDublinBikesLiveDataService @Inject constructor(
    private val dublinBikesLiveDataCache: LoadingCache<Pair<String, String>, DublinBikesLiveData>
) {

    fun getLiveData(dockId: String, apiKey: String): DublinBikesLiveData = dublinBikesLiveDataCache[Pair(dockId, apiKey)]
}
