package io.rtpi.resource

import com.google.inject.Inject
import io.rtpi.api.DockLiveData
import io.rtpi.api.DockLocation
import io.rtpi.service.dublinbikes.CachedDublinBikesDockService
import io.rtpi.service.dublinbikes.CachedDublinBikesLiveDataService
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.QueryParam
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

@Api(tags = ["Dublin Bikes"])
@Path("dublinbikes")
class DublinBikesResource @Inject constructor(
    private val dublinBikesDockService: CachedDublinBikesDockService,
    private val dublinBikesLiveDataService: CachedDublinBikesLiveDataService
) {

    @GET
    @Path("locations")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(
        value = "Get Dublin Bikes docks",
        notes = "Gets locations using the <a href=\"https://developer.jcdecaux.com/#/home/\">JCDecaux API</a>",
        response = DockLocation::class,
        responseContainer = "List"
    )
    fun getDublinBikesDocks(
        @QueryParam(value = "apiKey")
        @ApiParam(required = true)
        apiKey: String
    ): Response {
        return Response.ok(dublinBikesDockService.getDocks(apiKey)).build()
    }

    @GET
    @Path("livedata")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(
        value = "Get Dublin Bikes live data",
        notes = "Gets live data using the <a href=\"https://developer.jcdecaux.com/#/home/\">JCDecaux API</a>",
        response = DockLiveData::class
    )
    fun getDublinBikesLiveData(
        @QueryParam(value = "locationId")
        @ApiParam(required = true)
        locationId: String,
        @QueryParam(value = "apiKey")
        @ApiParam(required = true)
        apiKey: String
    ): Response {
        return Response.ok(dublinBikesLiveDataService.getLiveData(locationId, apiKey)).build()
    }
}
