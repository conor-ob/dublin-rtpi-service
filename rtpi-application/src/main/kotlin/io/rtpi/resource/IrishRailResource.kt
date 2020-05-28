package io.rtpi.resource

import com.google.inject.Inject
import io.rtpi.api.PredictionLiveData
import io.rtpi.api.StopLocation
import io.rtpi.service.irishrail.CachedIrishRailLiveDataService
import io.rtpi.service.irishrail.CachedIrishRailStationService
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.QueryParam
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

@Api(tags = ["Irish Rail"])
@Path("irishrail")
class IrishRailResource @Inject constructor(
    private val irishRailStationService: CachedIrishRailStationService,
    private val irishRailLiveDataService: CachedIrishRailLiveDataService
) {

    @GET
    @Path("locations")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(
        value = "Get Irish Rail stations",
        notes = "Gets locations using the <a href=\"http://api.irishrail.ie/realtime/\">Irish Rail API</a>",
        response = StopLocation::class,
        responseContainer = "List"
    )
    fun getIrishRailStations(): Response {
        return Response.ok(irishRailStationService.getStations()).build()
    }

    @GET
    @Path("livedata")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(
        value = "Get Irish Rail live data",
        notes = "Gets live data using the <a href=\"http://api.irishrail.ie/realtime/\">Irish Rail API</a>",
        response = PredictionLiveData::class,
        responseContainer = "List"
    )
    fun getIrishRailLiveData(
        @QueryParam(value = "locationId")
        @ApiParam(required = true)
        locationId: String
    ): Response {
        return Response.ok(irishRailLiveDataService.getLiveData(locationId)).build()
    }
}
