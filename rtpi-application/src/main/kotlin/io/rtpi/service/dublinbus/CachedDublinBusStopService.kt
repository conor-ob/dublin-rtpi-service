package io.rtpi.service.dublinbus

import com.google.common.cache.CacheBuilder
import com.google.common.cache.CacheLoader
import com.google.common.cache.LoadingCache
import com.google.inject.Inject
import io.rtpi.api.DublinBusStop
import io.rtpi.api.Service
import java.util.concurrent.TimeUnit

class CachedDublinBusStopService @Inject constructor(
    private val dublinBusStopService: DublinBusStopService
) {

    private val cache: LoadingCache<Service, List<DublinBusStop>> = CacheBuilder.newBuilder()
        .expireAfterWrite(60L, TimeUnit.MINUTES)
        .build(
            object : CacheLoader<Service, List<DublinBusStop>>() {
                override fun load(key: Service): List<DublinBusStop> = dublinBusStopService.getStops().blockingGet()
            }
        )

    fun getStops(): List<DublinBusStop> = cache[Service.DUBLIN_BUS]
}
