package io.rtpi.resource

import com.google.common.cache.CacheBuilder
import com.google.common.cache.CacheLoader
import com.google.common.cache.LoadingCache
import com.google.inject.Inject
import io.rtpi.api.IrishRailLiveData
import io.rtpi.api.IrishRailStation
import io.rtpi.api.Service
import io.rtpi.service.irishrail.IrishRailLiveDataService
import io.rtpi.service.irishrail.IrishRailStationService
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
import java.util.concurrent.TimeUnit
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.QueryParam
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

@Api(tags = ["Irish Rail"])
@Path("irishrail")
class IrishRailResource @Inject constructor(
    private val irishRailStationService: IrishRailStationService,
    private val irishRailLiveDataService: IrishRailLiveDataService
) {

    private val irishRailStationCache: LoadingCache<Service, List<IrishRailStation>> = CacheBuilder.newBuilder()
        .expireAfterWrite(60L, TimeUnit.MINUTES)
        .build(
            object : CacheLoader<Service, List<IrishRailStation>>() {
                override fun load(key: Service) = irishRailStationService.getStations().blockingGet()
            }
        )

    private val irishRailLiveDataCache: LoadingCache<String, List<IrishRailLiveData>> = CacheBuilder.newBuilder()
        .expireAfterWrite(30L, TimeUnit.SECONDS)
        .build(
            object : CacheLoader<String, List<IrishRailLiveData>>() {
                override fun load(key: String) = irishRailLiveDataService.getLiveData(key).blockingGet()
            }
        )

    @GET
    @Path("locations")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(
        value = "Get Irish Rail stations",
        notes = "Gets locations using the <a href=\"http://api.irishrail.ie/realtime/\">Irish Rail API</a>",
        response = IrishRailStation::class,
        responseContainer = "List"
    )
    fun getIrishRailStations(): Response {
        return Response.ok(irishRailStationCache[Service.IRISH_RAIL]).build()
    }

    @GET
    @Path("livedata")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(
        value = "Get Irish Rail live data",
        notes = "Gets live data using the <a href=\"http://api.irishrail.ie/realtime/\">Irish Rail API</a>",
        response = IrishRailLiveData::class,
        responseContainer = "List"
    )
    fun getIrishRailLiveData(
        @QueryParam(value = "locationId")
        @ApiParam(required = true)
        locationId: String
    ): Response {
        return Response.ok(irishRailLiveDataCache[locationId]).build()
    }
}
