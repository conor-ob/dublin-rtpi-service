package io.rtpi.module

import com.authzee.kotlinguice4.KotlinModule
import com.google.common.cache.CacheBuilder
import com.google.common.cache.CacheLoader
import com.google.common.cache.LoadingCache
import com.google.inject.Provides
import com.google.inject.Singleton
import io.rtpi.api.IrishRailStation
import io.rtpi.api.Service
import io.rtpi.service.irishrail.IrishRailStationService
import java.util.concurrent.TimeUnit

class CacheModule : KotlinModule() {

    @Provides
    @Singleton
    fun irishRailStationCache(
        irishRailStationService: IrishRailStationService
    ): LoadingCache<Service, List<IrishRailStation>> =
        CacheBuilder.newBuilder()
            .expireAfterWrite(60L, TimeUnit.MINUTES)
            .build(
                object : CacheLoader<Service, List<IrishRailStation>>() {
                    override fun load(key: Service): List<IrishRailStation> {
                        return irishRailStationService.getStations().blockingGet()
                    }
                }
            )

}
