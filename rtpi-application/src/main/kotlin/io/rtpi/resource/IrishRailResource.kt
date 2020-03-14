package io.rtpi.resource

import com.google.common.cache.CacheBuilder
import com.google.common.cache.CacheLoader
import com.google.common.cache.LoadingCache
import io.rtpi.api.IrishRailLiveData
import io.rtpi.api.IrishRailStation
import io.rtpi.api.Service
import io.rtpi.service.irishrail.IrishRailLiveDataService
import io.rtpi.service.irishrail.IrishRailStationService
import java.util.concurrent.TimeUnit
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.QueryParam
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

@Path("irishrail")
class IrishRailResource(
    private val irishRailStationService: IrishRailStationService,
    private val irishRailLiveDataService: IrishRailLiveDataService
) {

    private val stationsCache: LoadingCache<Service, List<IrishRailStation>> =
        CacheBuilder.newBuilder()
            .expireAfterWrite(60L, TimeUnit.MINUTES)
            .build(
                object : CacheLoader<Service, List<IrishRailStation>>() {
                    override fun load(key: Service): List<IrishRailStation> {
                        return irishRailStationService.getStations().blockingGet()
                    }
                }
            )

    private val liveDataCache: LoadingCache<String, List<IrishRailLiveData>> =
        CacheBuilder.newBuilder()
            .expireAfterWrite(30L, TimeUnit.SECONDS)
            .maximumSize(5000L)
            .build(
                object : CacheLoader<String, List<IrishRailLiveData>>() {
                    override fun load(key: String): List<IrishRailLiveData> {
                        return irishRailLiveDataService.getLiveData(key).blockingGet()
                    }
                }
            )

    @GET
    @Path("stations")
    @Produces(MediaType.APPLICATION_JSON)
    fun getStops(): Response {
        return Response.ok(stationsCache.get(Service.IRISH_RAIL)).build()
    }

    @GET
    @Path("livedata")
    @Produces(MediaType.APPLICATION_JSON)
    fun getLiveData(@QueryParam(value = "stationId") stationId: String): Response {
        return Response.ok(liveDataCache.get(stationId)).build()
    }

}
