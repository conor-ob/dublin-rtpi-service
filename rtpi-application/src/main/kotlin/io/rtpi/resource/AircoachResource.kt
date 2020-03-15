package io.rtpi.resource

import com.google.common.cache.CacheBuilder
import com.google.common.cache.CacheLoader
import com.google.common.cache.LoadingCache
import com.google.inject.Inject
import io.rtpi.api.AircoachLiveData
import io.rtpi.api.AircoachStop
import io.rtpi.api.Service
import io.rtpi.service.aircoach.AircoachLiveDataService
import io.rtpi.service.aircoach.AircoachStopService
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

@Api(tags = ["Aircoach"])
@Path("aircoach")
class AircoachResource @Inject constructor(
    private val aircoachStopService: AircoachStopService,
    private val aircoachLiveDataService: AircoachLiveDataService
) {

    private val aircoachStopCache: LoadingCache<Service, List<AircoachStop>> = CacheBuilder.newBuilder()
        .expireAfterWrite(60L, TimeUnit.MINUTES)
        .build(
            object : CacheLoader<Service, List<AircoachStop>>() {
                override fun load(key: Service) = aircoachStopService.getStops().blockingGet()
            }
        )

    private val aircoachLiveDataCache: LoadingCache<String, List<AircoachLiveData>> = CacheBuilder.newBuilder()
        .expireAfterWrite(30L, TimeUnit.SECONDS)
        .build(
            object : CacheLoader<String, List<AircoachLiveData>>() {
                override fun load(key: String) = aircoachLiveDataService.getLiveData(key).blockingGet()
            }
        )

    @GET
    @Path("locations")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(
        value = "Get Aircoach stops",
        notes = "Gets locations by scraping the <a href=\"https://tracker.aircoach.ie/stop-finder/\">Aircoach Stop Finder</a>",
        response = AircoachStop::class,
        responseContainer = "List"
    )
    fun getAircoachStops(): Response {
        return Response.ok(aircoachStopCache[Service.AIRCOACH]).build()
    }

    @GET
    @Path("livedata")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(
        value = "Get Aircoach live data",
        notes = "Gets live data using the <a href=\"https://tracker.aircoach.ie/\">Aircoach API</a>",
        response = AircoachLiveData::class,
        responseContainer = "List"
    )
    fun getAircoachLiveData(
        @QueryParam(value = "locationId")
        @ApiParam(required = true)
        locationId: String
    ): Response {
        return Response.ok(aircoachLiveDataCache[locationId]).build()
    }
}
