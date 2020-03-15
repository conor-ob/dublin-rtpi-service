package io.rtpi.resource

import com.google.inject.Inject
import io.rtpi.api.IrishRailLiveData
import io.rtpi.api.IrishRailStation
import io.rtpi.service.irishrail.IrishRailLiveDataService
import io.rtpi.service.irishrail.IrishRailStationService
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.QueryParam
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

@Api
@Path("irishrail")
class IrishRailResource @Inject constructor(
    private val irishRailStationService: IrishRailStationService,
    private val irishRailLiveDataService: IrishRailLiveDataService
) {

    @GET
    @Path("stations")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(
        value = "Get Irish Rail stations",
        response = IrishRailStation::class,
        responseContainer = "List"
    )
    fun getStops(): Response {
        return Response.ok(irishRailStationService.getStations().blockingGet()).build()
    }

    @GET
    @Path("livedata")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(
        value = "Get Irish Rail live data",
        response = IrishRailLiveData::class,
        responseContainer = "List"
    )
    fun getLiveData(
        @QueryParam(value = "stationId")
        @ApiParam(required = true)
        stationId: String
    ): Response {
        return Response.ok(irishRailLiveDataService.getLiveData(stationId).blockingGet()).build()
    }

}
