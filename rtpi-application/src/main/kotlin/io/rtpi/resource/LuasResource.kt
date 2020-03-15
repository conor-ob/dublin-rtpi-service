package io.rtpi.resource

import com.google.common.cache.CacheBuilder
import com.google.common.cache.CacheLoader
import com.google.common.cache.LoadingCache
import com.google.inject.Inject
import io.rtpi.api.LuasLiveData
import io.rtpi.api.LuasStop
import io.rtpi.api.Service
import io.rtpi.service.luas.LuasLiveDataService
import io.rtpi.service.luas.LuasStopService
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

@Api(tags = ["Luas"])
@Path("luas")
class LuasResource @Inject constructor(
    private val luasStopService: LuasStopService,
    private val luasLiveDataService: LuasLiveDataService
) {

    private val luasStopCache: LoadingCache<Service, List<LuasStop>> = CacheBuilder.newBuilder()
        .expireAfterWrite(60L, TimeUnit.MINUTES)
        .build(
            object : CacheLoader<Service, List<LuasStop>>() {
                override fun load(key: Service) = luasStopService.getStops().blockingGet()
            }
        )

    private val luasLiveDataCache: LoadingCache<String, List<LuasLiveData>> = CacheBuilder.newBuilder()
        .expireAfterWrite(30L, TimeUnit.SECONDS)
        .build(
            object : CacheLoader<String, List<LuasLiveData>>() {
                override fun load(key: String) = luasLiveDataService.getLiveData(key).blockingGet()
            }
        )

    @GET
    @Path("locations")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(
        value = "Get Luas stops",
        notes = "Gets locations using the <a href=\"https://data.smartdublin.ie/dataset/real-time-passenger-information-rtpi-for-dublin-bus-bus-eireann-luas-and-irish-rail/\">RTPI API</a>",
        response = LuasStop::class,
        responseContainer = "List"
    )
    fun getLuasStops(): Response {
        return Response.ok(luasStopCache[Service.LUAS]).build()
    }

    @GET
    @Path("livedata")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(
        value = "Get Luas live data",
        notes = "Gets live data using the <a href=\"https://data.smartdublin.ie/dataset/real-time-passenger-information-rtpi-for-dublin-bus-bus-eireann-luas-and-irish-rail/\">RTPI API</a>",
        response = LuasLiveData::class,
        responseContainer = "List"
    )
    fun getLuasLiveData(
        @QueryParam(value = "locationId")
        @ApiParam(required = true)
        locationId: String
    ): Response {
        return Response.ok(luasLiveDataCache[locationId]).build()
    }
}
