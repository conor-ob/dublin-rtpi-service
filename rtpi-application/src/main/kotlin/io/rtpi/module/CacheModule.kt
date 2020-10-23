package io.rtpi.module

import com.authzee.kotlinguice4.KotlinModule
import com.google.common.cache.CacheBuilder
import com.google.common.cache.CacheLoader
import com.google.common.cache.LoadingCache
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors
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
import org.slf4j.LoggerFactory
import java.util.concurrent.Callable
import java.util.concurrent.Executors

class CacheModule : KotlinModule() {

    @Provides
    @Singleton
    @Named("AIRCOACH")
    fun aircoachStopCache(
        aircoachStopService: AircoachStopService,
        rtpiServiceConfiguration: RtpiServiceConfiguration
    ): LoadingCache<Service, List<ServiceLocation>> = CacheBuilder
        .from(requireNotNull(rtpiServiceConfiguration.cacheConfiguration.serviceLocationCacheSpec))
        .build(
            object : CacheLoader<Service, List<ServiceLocation>>() {

                private val cacheReloadingExecutorService = MoreExecutors.listeningDecorator(
                    Executors.newFixedThreadPool(10)
                )

                override fun load(key: Service): List<ServiceLocation> {
                    return aircoachStopService.getStops().blockingGet()
                }

                override fun reload(
                    key: Service,
                    oldValue: List<ServiceLocation>
                ): ListenableFuture<List<ServiceLocation>> {
                    return cacheReloadingExecutorService.submit(Callable { load(key) })
                }
            }
        )

    @Provides
    @Singleton
    @Named("AIRCOACH")
    fun aircoachLiveDataCache(
        aircoachLiveDataService: AircoachLiveDataService,
        rtpiServiceConfiguration: RtpiServiceConfiguration
    ): LoadingCache<String, List<LiveData>> = CacheBuilder
        .from(requireNotNull(rtpiServiceConfiguration.cacheConfiguration.liveDataCacheSpec))
        .build(
            object : CacheLoader<String, List<LiveData>>() {

                private val cacheReloadingExecutorService = MoreExecutors.listeningDecorator(
                    Executors.newFixedThreadPool(10)
                )

                override fun load(key: String): List<LiveData> {
                    return aircoachLiveDataService.getLiveData(key).blockingGet()
                }

                override fun reload(key: String, oldValue: List<LiveData>): ListenableFuture<List<LiveData>> {
                    return cacheReloadingExecutorService.submit(Callable { load(key) })
                }
            }
        )

    @Provides
    @Singleton
    @Named("BUS_EIREANN")
    fun busEireannStopCache(
        busEireannStopService: BusEireannStopService,
        rtpiServiceConfiguration: RtpiServiceConfiguration
    ): LoadingCache<Service, List<ServiceLocation>> = CacheBuilder
        .from(requireNotNull(rtpiServiceConfiguration.cacheConfiguration.serviceLocationCacheSpec))
        .build(
            object : CacheLoader<Service, List<ServiceLocation>>() {

                private val cacheReloadingExecutorService = MoreExecutors.listeningDecorator(
                    Executors.newFixedThreadPool(10)
                )

                override fun load(key: Service): List<ServiceLocation> {
                    return busEireannStopService.getStops().blockingGet()
                }

                override fun reload(
                    key: Service,
                    oldValue: List<ServiceLocation>
                ): ListenableFuture<List<ServiceLocation>> {
                    return cacheReloadingExecutorService.submit(Callable { load(key) })
                }
            }
        )

    @Provides
    @Singleton
    @Named("BUS_EIREANN")
    fun busEireannLiveDataCache(
        busEireannLiveDataService: BusEireannLiveDataService,
        rtpiServiceConfiguration: RtpiServiceConfiguration
    ): LoadingCache<String, List<LiveData>> = CacheBuilder
        .from(requireNotNull(rtpiServiceConfiguration.cacheConfiguration.liveDataCacheSpec))
        .build(
            object : CacheLoader<String, List<LiveData>>() {

                private val cacheReloadingExecutorService = MoreExecutors.listeningDecorator(
                    Executors.newFixedThreadPool(10)
                )

                override fun load(key: String): List<LiveData> {
                    return busEireannLiveDataService.getLiveData(key).blockingGet()
                }

                override fun reload(key: String, oldValue: List<LiveData>): ListenableFuture<List<LiveData>> {
                    return cacheReloadingExecutorService.submit(Callable { load(key) })
                }
            }
        )

    @Provides
    @Singleton
    @Named("DUBLIN_BIKES")
    fun dublinBikesDockCache(
        dublinBikesDockService: DublinBikesDockService,
        rtpiServiceConfiguration: RtpiServiceConfiguration
    ): LoadingCache<String, List<ServiceLocation>> = CacheBuilder
        .from(requireNotNull(rtpiServiceConfiguration.cacheConfiguration.serviceLocationCacheSpec))
        .build(
            object : CacheLoader<String, List<ServiceLocation>>() {

                private val cacheReloadingExecutorService = MoreExecutors.listeningDecorator(
                    Executors.newFixedThreadPool(10)
                )

                override fun load(key: String): List<ServiceLocation> {
                    return dublinBikesDockService.getDocks(key).blockingGet()
                }

                override fun reload(
                    key: String,
                    oldValue: List<ServiceLocation>
                ): ListenableFuture<List<ServiceLocation>> {
                    return cacheReloadingExecutorService.submit(Callable { load(key) })
                }
            }
        )

    @Provides
    @Singleton
    @Named("DUBLIN_BIKES")
    fun dublinBikesLiveDataCache(
        dublinBikesLiveDataService: DublinBikesLiveDataService,
        rtpiServiceConfiguration: RtpiServiceConfiguration
    ): LoadingCache<Pair<String, String>, LiveData> = CacheBuilder
        .from(requireNotNull(rtpiServiceConfiguration.cacheConfiguration.liveDataCacheSpec))
        .build(
            object : CacheLoader<Pair<String, String>, LiveData>() {

                private val cacheReloadingExecutorService = MoreExecutors.listeningDecorator(
                    Executors.newFixedThreadPool(10)
                )

                override fun load(key: Pair<String, String>): LiveData {
                    return dublinBikesLiveDataService.getLiveData(key.first, key.second).blockingGet()
                }

                override fun reload(key: Pair<String, String>, oldValue: LiveData): ListenableFuture<LiveData> {
                    return cacheReloadingExecutorService.submit(Callable { load(key) })
                }
            }
        )

    @Provides
    @Singleton
    @Named("DUBLIN_BUS")
    fun dublinBusStopCache(
        dublinBusStopService: DublinBusStopService,
        rtpiServiceConfiguration: RtpiServiceConfiguration
    ): LoadingCache<Service, List<ServiceLocation>> = CacheBuilder
        .from(requireNotNull(rtpiServiceConfiguration.cacheConfiguration.serviceLocationCacheSpec))
        .build(
            object : CacheLoader<Service, List<ServiceLocation>>() {

                private val logger = LoggerFactory.getLogger(CacheModule::class.java)

                private val cacheReloadingExecutorService = MoreExecutors.listeningDecorator(
                    Executors.newFixedThreadPool(10)
                )

                override fun load(key: Service): List<ServiceLocation> {
                    logger.info("loading $key")
                    val result = dublinBusStopService.getStops().blockingGet()
                    logger.info("finished loading $key")
                    return result
                }

                override fun reload(
                    key: Service,
                    oldValue: List<ServiceLocation>
                ): ListenableFuture<List<ServiceLocation>> {
                    logger.info("reloading $key")
                    return cacheReloadingExecutorService.submit(Callable { load(key) })
                }
            }
        )

    @Provides
    @Singleton
    @Named("DUBLIN_BUS")
    fun dublinBusLiveDataCache(
        dublinBusLiveDataService: DublinBusLiveDataService,
        rtpiServiceConfiguration: RtpiServiceConfiguration
    ): LoadingCache<String, List<LiveData>> = CacheBuilder
        .from(requireNotNull(rtpiServiceConfiguration.cacheConfiguration.liveDataCacheSpec))
        .build(
            object : CacheLoader<String, List<LiveData>>() {

                private val cacheReloadingExecutorService = MoreExecutors.listeningDecorator(
                    Executors.newFixedThreadPool(10)
                )

                override fun load(key: String): List<LiveData> {
                    return dublinBusLiveDataService.getLiveData(key).blockingGet()
                }

                override fun reload(key: String, oldValue: List<LiveData>): ListenableFuture<List<LiveData>> {
                    return cacheReloadingExecutorService.submit(Callable { load(key) })
                }
            }
        )

    @Provides
    @Singleton
    @Named("IRISH_RAIL")
    fun irishRailStationCache(
        irishRailStationService: IrishRailStationService,
        rtpiServiceConfiguration: RtpiServiceConfiguration
    ): LoadingCache<Service, List<ServiceLocation>> = CacheBuilder
        .from(requireNotNull(rtpiServiceConfiguration.cacheConfiguration.serviceLocationCacheSpec))
        .build(
            object : CacheLoader<Service, List<ServiceLocation>>() {

                private val cacheReloadingExecutorService = MoreExecutors.listeningDecorator(
                    Executors.newFixedThreadPool(10)
                )

                override fun load(key: Service): List<ServiceLocation> {
                    return irishRailStationService.getStations().blockingGet()
                }

                override fun reload(
                    key: Service,
                    oldValue: List<ServiceLocation>
                ): ListenableFuture<List<ServiceLocation>> {
                    return cacheReloadingExecutorService.submit(Callable { load(key) })
                }
            }
        )

    @Provides
    @Singleton
    @Named("IRISH_RAIL")
    fun irishRailLiveDataCache(
        irishRailLiveDataService: IrishRailLiveDataService,
        rtpiServiceConfiguration: RtpiServiceConfiguration
    ): LoadingCache<String, List<LiveData>> = CacheBuilder
        .from(requireNotNull(rtpiServiceConfiguration.cacheConfiguration.liveDataCacheSpec))
        .build(
            object : CacheLoader<String, List<LiveData>>() {

                private val cacheReloadingExecutorService = MoreExecutors.listeningDecorator(
                    Executors.newFixedThreadPool(10)
                )

                override fun load(key: String): List<LiveData> {
                    return irishRailLiveDataService.getLiveData(key).blockingGet()
                }

                override fun reload(key: String, oldValue: List<LiveData>): ListenableFuture<List<LiveData>> {
                    return cacheReloadingExecutorService.submit(Callable { load(key) })
                }
            }
        )

    @Provides
    @Singleton
    @Named("LUAS")
    fun luasStopCache(
        luasStopService: LuasStopService,
        rtpiServiceConfiguration: RtpiServiceConfiguration
    ): LoadingCache<Service, List<ServiceLocation>> = CacheBuilder
        .from(requireNotNull(rtpiServiceConfiguration.cacheConfiguration.serviceLocationCacheSpec))
        .build(
            object : CacheLoader<Service, List<ServiceLocation>>() {

                private val cacheReloadingExecutorService = MoreExecutors.listeningDecorator(
                    Executors.newFixedThreadPool(10)
                )

                override fun load(key: Service): List<ServiceLocation> {
                    return luasStopService.getStops().blockingGet()
                }

                override fun reload(
                    key: Service,
                    oldValue: List<ServiceLocation>
                ): ListenableFuture<List<ServiceLocation>> {
                    return cacheReloadingExecutorService.submit(Callable { load(key) })
                }
            }
        )

    @Provides
    @Singleton
    @Named("LUAS")
    fun luasLiveDataCache(
        luasLiveDataService: LuasLiveDataService,
        rtpiServiceConfiguration: RtpiServiceConfiguration
    ): LoadingCache<String, List<LiveData>> = CacheBuilder
        .from(requireNotNull(rtpiServiceConfiguration.cacheConfiguration.liveDataCacheSpec))
        .build(
            object : CacheLoader<String, List<LiveData>>() {

                private val cacheReloadingExecutorService = MoreExecutors.listeningDecorator(
                    Executors.newFixedThreadPool(10)
                )

                override fun load(key: String): List<LiveData> {
                    return luasLiveDataService.getLiveData(key).blockingGet()
                }

                override fun reload(key: String, oldValue: List<LiveData>): ListenableFuture<List<LiveData>> {
                    return cacheReloadingExecutorService.submit(Callable { load(key) })
                }
            }
        )
}
