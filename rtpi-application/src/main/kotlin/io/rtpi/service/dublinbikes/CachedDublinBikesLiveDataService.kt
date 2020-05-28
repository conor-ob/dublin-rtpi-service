package io.rtpi.service.dublinbikes

import com.google.common.cache.LoadingCache
import com.google.inject.Inject
import com.google.inject.name.Named
import io.rtpi.api.LiveData

class CachedDublinBikesLiveDataService @Inject constructor(
    @Named("DUBLIN_BIKES") private val dublinBikesLiveDataCache: LoadingCache<Pair<String, String>, LiveData>
) {

    fun getLiveData(dockId: String, apiKey: String): LiveData = dublinBikesLiveDataCache[Pair(dockId, apiKey)]
}
