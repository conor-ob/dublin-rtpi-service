package io.rtpi.resource

import com.google.inject.Inject
import io.rtpi.api.LiveData
import io.rtpi.api.StopLocation
import io.rtpi.service.luas.CachedLuasLiveDataService
import io.rtpi.service.luas.CachedLuasStopService
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.QueryParam
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

@Api(tags = ["Luas"])
@Path("luas")
class LuasResource @Inject constructor(
    private val luasStopService: CachedLuasStopService,
    private val luasLiveDataService: CachedLuasLiveDataService
) {

    @GET
    @Path("locations")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(
        value = "Get Luas stops",
        notes = "Gets locations using the <a href=\"https://data.smartdublin.ie/dataset/real-time-passenger-information-rtpi-for-dublin-bus-bus-eireann-luas-and-irish-rail/\">RTPI API</a>",
        response = StopLocation::class,
        responseContainer = "List"
    )
    fun getLuasStops(): Response {
        return Response.ok(luasStopService.getStops()).build()
    }

    @GET
    @Path("livedata")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(
        value = "Get Luas live data",
        notes = "Gets live data using the <a href=\"https://data.smartdublin.ie/dataset/real-time-passenger-information-rtpi-for-dublin-bus-bus-eireann-luas-and-irish-rail/\">RTPI API</a>",
        response = LiveData::class,
        responseContainer = "List"
    )
    fun getLuasLiveData(
        @QueryParam(value = "locationId")
        @ApiParam(required = true)
        locationId: String
    ): Response {
        return Response.ok(luasLiveDataService.getLiveData(locationId)).build()
    }
}
