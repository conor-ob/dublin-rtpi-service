package io.rtpi.service.dublinbikes

import com.google.common.cache.LoadingCache
import com.google.inject.Inject
import io.rtpi.api.DublinBikesDock

class CachedDublinBikesDockService @Inject constructor(
    private val dublinBikesDockCache: LoadingCache<String, List<DublinBikesDock>>
) {

    fun getDocks(apiKey: String): List<DublinBikesDock> = dublinBikesDockCache[apiKey]
}
