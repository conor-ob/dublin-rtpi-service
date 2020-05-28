package io.rtpi.module

import com.authzee.kotlinguice4.KotlinModule
import com.google.common.cache.CacheBuilder
import com.google.common.cache.CacheLoader
import com.google.common.cache.LoadingCache
import com.google.inject.Provides
import com.google.inject.Singleton
import com.google.inject.name.Named
import io.rtpi.RtpiServiceConfiguration
import io.rtpi.api.LiveData
import io.rtpi.api.Service
import io.rtpi.api.ServiceLocation
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
    @Named("AIRCOACH")
    fun aircoachStopCache(
        aircoachStopService: AircoachStopService,
        rtpiServiceConfiguration: RtpiServiceConfiguration
    ): LoadingCache<Service, List<ServiceLocation>> = CacheBuilder.newBuilder()
        .expireAfterWrite(
            requireNotNull(rtpiServiceConfiguration.cacheConfiguration.serviceLocationExpiry?.seconds),
            TimeUnit.SECONDS
        )
        .build(
            object : CacheLoader<Service, List<ServiceLocation>>() {
                override fun load(key: Service): List<ServiceLocation> {
                    return aircoachStopService.getStops().blockingGet()
                }
            }
        )

    @Provides
    @Singleton
    @Named("AIRCOACH")
    fun aircoachLiveDataCache(
        aircoachLiveDataService: AircoachLiveDataService,
        rtpiServiceConfiguration: RtpiServiceConfiguration
    ): LoadingCache<String, List<LiveData>> = CacheBuilder.newBuilder()
        .expireAfterWrite(
            requireNotNull(rtpiServiceConfiguration.cacheConfiguration.liveDataExpiry?.seconds),
            TimeUnit.SECONDS
        )
        .build(
            object : CacheLoader<String, List<LiveData>>() {
                override fun load(key: String): List<LiveData> {
                    return aircoachLiveDataService.getLiveData(key).blockingGet()
                }
            }
        )

    @Provides
    @Singleton
    @Named("BUS_EIREANN")
    fun busEireannStopCache(
        busEireannStopService: BusEireannStopService,
        rtpiServiceConfiguration: RtpiServiceConfiguration
    ): LoadingCache<Service, List<ServiceLocation>> = CacheBuilder.newBuilder()
        .expireAfterWrite(
            requireNotNull(rtpiServiceConfiguration.cacheConfiguration.serviceLocationExpiry?.seconds),
            TimeUnit.SECONDS
        )
        .build(
            object : CacheLoader<Service, List<ServiceLocation>>() {
                override fun load(key: Service): List<ServiceLocation> {
                    return busEireannStopService.getStops().blockingGet()
                }
            }
        )

    @Provides
    @Singleton
    @Named("BUS_EIREANN")
    fun busEireannLiveDataCache(
        busEireannLiveDataService: BusEireannLiveDataService,
        rtpiServiceConfiguration: RtpiServiceConfiguration
    ): LoadingCache<String, List<LiveData>> = CacheBuilder.newBuilder()
        .expireAfterWrite(
            requireNotNull(rtpiServiceConfiguration.cacheConfiguration.liveDataExpiry?.seconds),
            TimeUnit.SECONDS
        )
        .build(
            object : CacheLoader<String, List<LiveData>>() {
                override fun load(key: String): List<LiveData> {
                    return busEireannLiveDataService.getLiveData(key).blockingGet()
                }
            }
        )

    @Provides
    @Singleton
    @Named("DUBLIN_BIKES")
    fun dublinBikesDockCache(
        dublinBikesDockService: DublinBikesDockService,
        rtpiServiceConfiguration: RtpiServiceConfiguration
    ): LoadingCache<String, List<ServiceLocation>> = CacheBuilder.newBuilder()
        .expireAfterWrite(
            requireNotNull(rtpiServiceConfiguration.cacheConfiguration.liveDataExpiry?.seconds),
            TimeUnit.SECONDS
        )
        .build(
            object : CacheLoader<String, List<ServiceLocation>>() {
                override fun load(key: String): List<ServiceLocation> {
                    return dublinBikesDockService.getDocks(key).blockingGet()
                }
            }
        )

    @Provides
    @Singleton
    @Named("DUBLIN_BIKES")
    fun dublinBikesLiveDataCache(
        dublinBikesLiveDataService: DublinBikesLiveDataService,
        rtpiServiceConfiguration: RtpiServiceConfiguration
    ): LoadingCache<Pair<String, String>, LiveData> = CacheBuilder.newBuilder()
        .expireAfterWrite(
            requireNotNull(rtpiServiceConfiguration.cacheConfiguration.liveDataExpiry?.seconds),
            TimeUnit.SECONDS
        )
        .build(
            object : CacheLoader<Pair<String, String>, LiveData>() {
                override fun load(key: Pair<String, String>): LiveData {
                    return dublinBikesLiveDataService.getLiveData(key.first, key.second).blockingGet()
                }
            }
        )

    @Provides
    @Singleton
    @Named("DUBLIN_BUS")
    fun dublinBusStopCache(
        dublinBusStopService: DublinBusStopService,
        rtpiServiceConfiguration: RtpiServiceConfiguration
    ): LoadingCache<Service, List<ServiceLocation>> = CacheBuilder.newBuilder()
        .expireAfterWrite(
            requireNotNull(rtpiServiceConfiguration.cacheConfiguration.serviceLocationExpiry?.seconds),
            TimeUnit.SECONDS
        )
        .build(
            object : CacheLoader<Service, List<ServiceLocation>>() {
                override fun load(key: Service): List<ServiceLocation> {
                    return dublinBusStopService.getStops().blockingGet()
                }
            }
        )

    @Provides
    @Singleton
    @Named("DUBLIN_BUS")
    fun dublinBusLiveDataCache(
        dublinBusLiveDataService: DublinBusLiveDataService,
        rtpiServiceConfiguration: RtpiServiceConfiguration
    ): LoadingCache<String, List<LiveData>> = CacheBuilder.newBuilder()
        .expireAfterWrite(
            requireNotNull(rtpiServiceConfiguration.cacheConfiguration.liveDataExpiry?.seconds),
            TimeUnit.SECONDS
        )
        .build(
            object : CacheLoader<String, List<LiveData>>() {
                override fun load(key: String): List<LiveData> {
                    return dublinBusLiveDataService.getLiveData(key).blockingGet()
                }
            }
        )

    @Provides
    @Singleton
    @Named("IRISH_RAIL")
    fun irishRailStationCache(
        irishRailStationService: IrishRailStationService,
        rtpiServiceConfiguration: RtpiServiceConfiguration
    ): LoadingCache<Service, List<ServiceLocation>> = CacheBuilder.newBuilder()
        .expireAfterWrite(
            requireNotNull(rtpiServiceConfiguration.cacheConfiguration.serviceLocationExpiry?.seconds),
            TimeUnit.SECONDS
        )
        .build(
            object : CacheLoader<Service, List<ServiceLocation>>() {
                override fun load(key: Service): List<ServiceLocation> {
                    return irishRailStationService.getStations().blockingGet()
                }
            }
        )

    @Provides
    @Singleton
    @Named("IRISH_RAIL")
    fun irishRailLiveDataCache(
        irishRailLiveDataService: IrishRailLiveDataService,
        rtpiServiceConfiguration: RtpiServiceConfiguration
    ): LoadingCache<String, List<LiveData>> = CacheBuilder.newBuilder()
        .expireAfterWrite(
            requireNotNull(rtpiServiceConfiguration.cacheConfiguration.liveDataExpiry?.seconds),
            TimeUnit.SECONDS
        )
        .build(
            object : CacheLoader<String, List<LiveData>>() {
                override fun load(key: String): List<LiveData> {
                    return irishRailLiveDataService.getLiveData(key).blockingGet()
                }
            }
        )

    @Provides
    @Singleton
    @Named("LUAS")
    fun luasStopCache(
        luasStopService: LuasStopService,
        rtpiServiceConfiguration: RtpiServiceConfiguration
    ): LoadingCache<Service, List<ServiceLocation>> = CacheBuilder.newBuilder()
        .expireAfterWrite(
            requireNotNull(rtpiServiceConfiguration.cacheConfiguration.serviceLocationExpiry?.seconds),
            TimeUnit.SECONDS
        )
        .build(
            object : CacheLoader<Service, List<ServiceLocation>>() {
                override fun load(key: Service): List<ServiceLocation> {
                    return luasStopService.getStops().blockingGet()
                }
            }
        )

    @Provides
    @Singleton
    @Named("LUAS")
    fun luasLiveDataCache(
        luasLiveDataService: LuasLiveDataService,
        rtpiServiceConfiguration: RtpiServiceConfiguration
    ): LoadingCache<String, List<LiveData>> = CacheBuilder.newBuilder()
        .expireAfterWrite(
            requireNotNull(rtpiServiceConfiguration.cacheConfiguration.liveDataExpiry?.seconds),
            TimeUnit.SECONDS
        )
        .build(
            object : CacheLoader<String, List<LiveData>>() {
                override fun load(key: String): List<LiveData> {
                    return luasLiveDataService.getLiveData(key).blockingGet()
                }
            }
        )
}
