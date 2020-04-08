package io.rtpi.service.dublinbikes

import com.google.common.cache.LoadingCache
import com.google.inject.Inject
import com.google.inject.name.Named
import io.rtpi.api.ServiceLocation

class CachedDublinBikesDockService @Inject constructor(
    @Named("DUBLIN_BIKES") private val dublinBikesDockCache: LoadingCache<String, List<ServiceLocation>>
) {

    fun getDocks(apiKey: String): List<ServiceLocation> = dublinBikesDockCache[apiKey]
}
