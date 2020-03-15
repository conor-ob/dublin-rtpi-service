package io.rtpi.resource

import com.google.common.cache.CacheBuilder
import com.google.common.cache.CacheLoader
import com.google.common.cache.LoadingCache
import com.google.inject.Inject
import io.rtpi.api.DublinBikesDock
import io.rtpi.api.DublinBikesLiveData
import io.rtpi.service.dublinbikes.DublinBikesDockService
import io.rtpi.service.dublinbikes.DublinBikesLiveDataService
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

@Api(tags = ["Dublin Bikes"])
@Path("dublinbikes")
class DublinBikesResource @Inject constructor(
    private val dublinBikesDockService: DublinBikesDockService,
    private val dublinBikesLiveDataService: DublinBikesLiveDataService
) {

    private val dublinBikesDockCache: LoadingCache<String, List<DublinBikesDock>> = CacheBuilder.newBuilder()
        .expireAfterWrite(60L, TimeUnit.MINUTES)
        .build(
            object : CacheLoader<String, List<DublinBikesDock>>() {
                override fun load(key: String) = dublinBikesDockService.getDocks(key).blockingGet()
            }
        )

    private val dublinBikesLiveDataCache: LoadingCache<Pair<String, String>, DublinBikesLiveData> = CacheBuilder.newBuilder()
        .expireAfterWrite(30L, TimeUnit.SECONDS)
        .build(
            object : CacheLoader<Pair<String, String>, DublinBikesLiveData>() {
                override fun load(key: Pair<String, String>) = dublinBikesLiveDataService.getLiveData(
                    dockId = key.first, apiKey = key.second
                ).blockingGet()
            }
        )

    @GET
    @Path("locations")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(
        value = "Get Dublin Bikes docks",
        notes = "Gets locations using the <a href=\"https://developer.jcdecaux.com/#/home/\">JCDecaux API</a>",
        response = DublinBikesDock::class,
        responseContainer = "List"
    )
    fun getDublinBikesDocks(
        @QueryParam(value = "apiKey")
        @ApiParam(required = true)
        apiKey: String
    ): Response {
        return Response.ok(dublinBikesDockCache[apiKey]).build()
    }

    @GET
    @Path("livedata")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(
        value = "Get Dublin Bikes live data",
        notes = "Gets live data using the <a href=\"https://developer.jcdecaux.com/#/home/\">JCDecaux API</a>",
        response = DublinBikesLiveData::class
    )
    fun getDublinBikesLiveData(
        @QueryParam(value = "locationId")
        @ApiParam(required = true)
        locationId: String,
        @QueryParam(value = "apiKey")
        @ApiParam(required = true)
        apiKey: String
    ): Response {
        return Response.ok(dublinBikesLiveDataCache[Pair(locationId, apiKey)]).build()
    }
}
