package io.rtpi.resource

import com.google.common.cache.CacheBuilder
import com.google.common.cache.CacheLoader
import com.google.common.cache.LoadingCache
import com.google.inject.Inject
import io.rtpi.api.BusEireannLiveData
import io.rtpi.api.BusEireannStop
import io.rtpi.api.Service
import io.rtpi.service.buseireann.BusEireannLiveDataService
import io.rtpi.service.buseireann.BusEireannStopService
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

@Api(tags = ["Bus Éireann"])
@Path("buseireann")
class BusEireannResource @Inject constructor(
    private val busEireannStopService: BusEireannStopService,
    private val busEireannLiveDataService: BusEireannLiveDataService
) {

    private val busEireannStopCache: LoadingCache<Service, List<BusEireannStop>> = CacheBuilder.newBuilder()
        .expireAfterWrite(60L, TimeUnit.MINUTES)
        .build(
            object : CacheLoader<Service, List<BusEireannStop>>() {
                override fun load(key: Service) = busEireannStopService.getStops().blockingGet()
            }
        )

    private val busEireannLiveDataCache: LoadingCache<String, List<BusEireannLiveData>> = CacheBuilder.newBuilder()
        .expireAfterWrite(30L, TimeUnit.SECONDS)
        .build(
            object : CacheLoader<String, List<BusEireannLiveData>>() {
                override fun load(key: String) = busEireannLiveDataService.getLiveData(key).blockingGet()
            }
        )

    @GET
    @Path("locations")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(
        value = "Get Bus Éireann stops",
        notes = "Gets locations using the <a href=\"https://data.smartdublin.ie/dataset/real-time-passenger-information-rtpi-for-dublin-bus-bus-eireann-luas-and-irish-rail/\">RTPI API</a>",
        response = BusEireannStop::class,
        responseContainer = "List"
    )
    fun getBusEireannStops(): Response {
        return Response.ok(busEireannStopCache[Service.BUS_EIREANN]).build()
    }

    @GET
    @Path("livedata")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(
        value = "Get Bus Éireann live data",
        notes = "Gets live data using the <a href=\"https://data.smartdublin.ie/dataset/real-time-passenger-information-rtpi-for-dublin-bus-bus-eireann-luas-and-irish-rail/\">RTPI API</a>",
        response = BusEireannLiveData::class,
        responseContainer = "List"
    )
    fun getBusEireannLiveData(
        @QueryParam(value = "locationId")
        @ApiParam(required = true)
        locationId: String
    ): Response {
        return Response.ok(busEireannLiveDataCache[locationId]).build()
    }
}
