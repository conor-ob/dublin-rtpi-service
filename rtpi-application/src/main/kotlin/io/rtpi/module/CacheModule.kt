package io.rtpi.module

import com.authzee.kotlinguice4.KotlinModule
import com.google.common.cache.CacheBuilder
import com.google.common.cache.CacheLoader
import com.google.common.cache.LoadingCache
import com.google.inject.Provides
import com.google.inject.Singleton
import io.rtpi.RtpiServiceConfiguration
import io.rtpi.api.AircoachLiveData
import io.rtpi.api.AircoachStop
import io.rtpi.api.BusEireannLiveData
import io.rtpi.api.BusEireannStop
import io.rtpi.api.DublinBikesDock
import io.rtpi.api.DublinBikesLiveData
import io.rtpi.api.DublinBusLiveData
import io.rtpi.api.DublinBusStop
import io.rtpi.api.IrishRailLiveData
import io.rtpi.api.IrishRailStation
import io.rtpi.api.LuasLiveData
import io.rtpi.api.LuasStop
import io.rtpi.api.Service
import io.rtpi.service.aircoach.AircoachLiveDataService
import io.rtpi.service.aircoach.AircoachStopService
import io.rtpi.service.buseireann.BusEireannLiveDataService
import io.rtpi.service.buseireann.BusEireannStopService
import io.rtpi.service.dublinbikes.DublinBikesDockService
import io.rtpi.service.dublinbikes.DublinBikesLiveDataService
import io.rtpi.service.dublinbus.DublinBusLiveDataService
import io.rtpi.service.dublinbus.DublinBusStopService
import io.rtpi.service.irishrail.IrishRailLiveDataService
import io.rtpi.service.irishrail.IrishRailStationService
import io.rtpi.service.luas.LuasLiveDataService
import io.rtpi.service.luas.LuasStopService
import java.util.concurrent.TimeUnit

class CacheModule : KotlinModule() {

    @Provides
    @Singleton
    fun aircoachStopCache(
        aircoachStopService: AircoachStopService,
        rtpiServiceConfiguration: RtpiServiceConfiguration
    ): LoadingCache<Service, List<AircoachStop>> = CacheBuilder.newBuilder()
        .expireAfterWrite(
            requireNotNull(rtpiServiceConfiguration.cacheConfiguration.serviceLocationExpiry?.seconds),
            TimeUnit.SECONDS
        )
        .build(
            object : CacheLoader<Service, List<AircoachStop>>() {
                override fun load(key: Service): List<AircoachStop> {
                    return aircoachStopService.getStops().blockingGet()
                }
            }
        )

    @Provides
    @Singleton
    fun aircoachLiveDataCache(
        aircoachLiveDataService: AircoachLiveDataService,
        rtpiServiceConfiguration: RtpiServiceConfiguration
    ): LoadingCache<String, List<AircoachLiveData>> = CacheBuilder.newBuilder()
        .expireAfterWrite(
            requireNotNull(rtpiServiceConfiguration.cacheConfiguration.liveDataExpiry?.seconds),
            TimeUnit.SECONDS
        )
        .build(
            object : CacheLoader<String, List<AircoachLiveData>>() {
                override fun load(key: String): List<AircoachLiveData> {
                    return aircoachLiveDataService.getLiveData(key).blockingGet()
                }
            }
        )

    @Provides
    @Singleton
    fun busEireannStopCache(
        busEireannStopService: BusEireannStopService,
        rtpiServiceConfiguration: RtpiServiceConfiguration
    ): LoadingCache<Service, List<BusEireannStop>> = CacheBuilder.newBuilder()
        .expireAfterWrite(
            requireNotNull(rtpiServiceConfiguration.cacheConfiguration.serviceLocationExpiry?.seconds),
            TimeUnit.SECONDS
        )
        .build(
            object : CacheLoader<Service, List<BusEireannStop>>() {
                override fun load(key: Service): List<BusEireannStop> {
                    return busEireannStopService.getStops().blockingGet()
                }
            }
        )

    @Provides
    @Singleton
    fun busEireannLiveDataCache(
        busEireannLiveDataService: BusEireannLiveDataService,
        rtpiServiceConfiguration: RtpiServiceConfiguration
    ): LoadingCache<String, List<BusEireannLiveData>> = CacheBuilder.newBuilder()
        .expireAfterWrite(
            requireNotNull(rtpiServiceConfiguration.cacheConfiguration.liveDataExpiry?.seconds),
            TimeUnit.SECONDS
        )
        .build(
            object : CacheLoader<String, List<BusEireannLiveData>>() {
                override fun load(key: String): List<BusEireannLiveData> {
                    return busEireannLiveDataService.getLiveData(key).blockingGet()
                }
            }
        )

    @Provides
    @Singleton
    fun dublinBikesDockCache(
        dublinBikesDockService: DublinBikesDockService,
        rtpiServiceConfiguration: RtpiServiceConfiguration
    ): LoadingCache<String, List<DublinBikesDock>> = CacheBuilder.newBuilder()
        .expireAfterWrite(
            requireNotNull(rtpiServiceConfiguration.cacheConfiguration.liveDataExpiry?.seconds),
            TimeUnit.SECONDS
        )
        .build(
            object : CacheLoader<String, List<DublinBikesDock>>() {
                override fun load(key: String): List<DublinBikesDock> {
                    return dublinBikesDockService.getDocks(key).blockingGet()
                }
            }
        )

    @Provides
    @Singleton
    fun dublinBikesLiveDataCache(
        dublinBikesLiveDataService: DublinBikesLiveDataService,
        rtpiServiceConfiguration: RtpiServiceConfiguration
    ): LoadingCache<Pair<String, String>, DublinBikesLiveData> = CacheBuilder.newBuilder()
        .expireAfterWrite(
            requireNotNull(rtpiServiceConfiguration.cacheConfiguration.liveDataExpiry?.seconds),
            TimeUnit.SECONDS
        )
        .build(
            object : CacheLoader<Pair<String, String>, DublinBikesLiveData>() {
                override fun load(key: Pair<String, String>): DublinBikesLiveData {
                    return dublinBikesLiveDataService.getLiveData(key.first, key.second).blockingGet()
                }
            }
        )

    @Provides
    @Singleton
    fun dublinBusStopCache(
        dublinBusStopService: DublinBusStopService,
        rtpiServiceConfiguration: RtpiServiceConfiguration
    ): LoadingCache<Service, List<DublinBusStop>> = CacheBuilder.newBuilder()
        .expireAfterWrite(
            requireNotNull(rtpiServiceConfiguration.cacheConfiguration.serviceLocationExpiry?.seconds),
            TimeUnit.SECONDS
        )
        .build(
            object : CacheLoader<Service, List<DublinBusStop>>() {
                override fun load(key: Service): List<DublinBusStop> {
                    return dublinBusStopService.getStops().blockingGet()
                }
            }
        )

    @Provides
    @Singleton
    fun dublinBusLiveDataCache(
        dublinBusLiveDataService: DublinBusLiveDataService,
        rtpiServiceConfiguration: RtpiServiceConfiguration
    ): LoadingCache<String, List<DublinBusLiveData>> = CacheBuilder.newBuilder()
        .expireAfterWrite(
            requireNotNull(rtpiServiceConfiguration.cacheConfiguration.liveDataExpiry?.seconds),
            TimeUnit.SECONDS
        )
        .build(
            object : CacheLoader<String, List<DublinBusLiveData>>() {
                override fun load(key: String): List<DublinBusLiveData> {
                    return dublinBusLiveDataService.getLiveData(key).blockingGet()
                }
            }
        )

    @Provides
    @Singleton
    fun irishRailStationCache(
        irishRailStationService: IrishRailStationService,
        rtpiServiceConfiguration: RtpiServiceConfiguration
    ): LoadingCache<Service, List<IrishRailStation>> = CacheBuilder.newBuilder()
        .expireAfterWrite(
            requireNotNull(rtpiServiceConfiguration.cacheConfiguration.serviceLocationExpiry?.seconds),
            TimeUnit.SECONDS
        )
        .build(
            object : CacheLoader<Service, List<IrishRailStation>>() {
                override fun load(key: Service): List<IrishRailStation> {
                    return irishRailStationService.getStations().blockingGet()
                }
            }
        )

    @Provides
    @Singleton
    fun irishRailLiveDataCache(
        irishRailLiveDataService: IrishRailLiveDataService,
        rtpiServiceConfiguration: RtpiServiceConfiguration
    ): LoadingCache<String, List<IrishRailLiveData>> = CacheBuilder.newBuilder()
        .expireAfterWrite(
            requireNotNull(rtpiServiceConfiguration.cacheConfiguration.liveDataExpiry?.seconds),
            TimeUnit.SECONDS
        )
        .build(
            object : CacheLoader<String, List<IrishRailLiveData>>() {
                override fun load(key: String): List<IrishRailLiveData> {
                    return irishRailLiveDataService.getLiveData(key).blockingGet()
                }
            }
        )

    @Provides
    @Singleton
    fun luasStopCache(
        luasStopService: LuasStopService,
        rtpiServiceConfiguration: RtpiServiceConfiguration
    ): LoadingCache<Service, List<LuasStop>> = CacheBuilder.newBuilder()
        .expireAfterWrite(
            requireNotNull(rtpiServiceConfiguration.cacheConfiguration.serviceLocationExpiry?.seconds),
            TimeUnit.SECONDS
        )
        .build(
            object : CacheLoader<Service, List<LuasStop>>() {
                override fun load(key: Service): List<LuasStop> {
                    return luasStopService.getStops().blockingGet()
                }
            }
        )

    @Provides
    @Singleton
    fun luasLiveDataCache(
        luasLiveDataService: LuasLiveDataService,
        rtpiServiceConfiguration: RtpiServiceConfiguration
    ): LoadingCache<String, List<LuasLiveData>> = CacheBuilder.newBuilder()
        .expireAfterWrite(
            requireNotNull(rtpiServiceConfiguration.cacheConfiguration.liveDataExpiry?.seconds),
            TimeUnit.SECONDS
        )
        .build(
            object : CacheLoader<String, List<LuasLiveData>>() {
                override fun load(key: String): List<LuasLiveData> {
                    return luasLiveDataService.getLiveData(key).blockingGet()
                }
            }
        )
}
