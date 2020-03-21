package io.rtpi.resource

import com.google.common.cache.CacheBuilder
import com.google.common.cache.CacheLoader
import com.google.common.cache.LoadingCache
import com.google.inject.Inject
import io.rtpi.api.DublinBusLiveData
import io.rtpi.api.DublinBusStop
import io.rtpi.service.dublinbus.CachedDublinBusStopService
import io.rtpi.service.dublinbus.DublinBusLiveDataService
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

@Api(tags = ["Dublin Bus"])
@Path("dublinbus")
class DublinBusResource @Inject constructor(
    private val dublinBusStopService: CachedDublinBusStopService,
    private val dublinBusLiveDataService: DublinBusLiveDataService
) {

    private val dublinBusLiveDataCache: LoadingCache<String, List<DublinBusLiveData>> = CacheBuilder.newBuilder()
        .expireAfterWrite(30L, TimeUnit.SECONDS)
        .build(
            object : CacheLoader<String, List<DublinBusLiveData>>() {
                override fun load(key: String) = dublinBusLiveDataService.getLiveData(key).blockingGet()
            }
        )

    @GET
    @Path("locations")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(
        value = "Get Dublin Bus stops",
        notes = "Gets locations using the <a href=\"https://data.smartdublin.ie/dataset/real-time-passenger-information-rtpi-for-dublin-bus-bus-eireann-luas-and-irish-rail/\">RTPI API</a>",
        response = DublinBusStop::class,
        responseContainer = "List"
    )
    fun getDublinBusStops(): Response {
        return Response.ok(dublinBusStopService.getStops()).build()
    }

    @GET
    @Path("livedata")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(
        value = "Get Dublin Bus live data",
        notes = "Gets live data using the <a href=\"https://data.smartdublin.ie/dataset/real-time-passenger-information-rtpi-for-dublin-bus-bus-eireann-luas-and-irish-rail/\">RTPI API</a>",
        response = DublinBusLiveData::class,
        responseContainer = "List"
    )
    fun getDublinBusLiveData(
        @QueryParam(value = "locationId")
        @ApiParam(required = true)
        locationId: String
    ): Response {
        return Response.ok(dublinBusLiveDataCache[locationId]).build()
    }
}
