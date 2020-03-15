package io.rtpi.resource

import com.google.inject.Inject
import io.rtpi.api.AircoachLiveData
import io.rtpi.api.AircoachStop
import io.rtpi.service.aircoach.AircoachLiveDataService
import io.rtpi.service.aircoach.AircoachStopService
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
@Path("aircoach")
class AircoachResource @Inject constructor(
    private val aircoachStopService: AircoachStopService,
    private val aircoachLiveDataService: AircoachLiveDataService
) {

    @GET
    @Path("stops")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(
        value = "Get Aircoach stops",
        response = AircoachStop::class,
        responseContainer = "List"
    )
    fun getStops(): Response {
        return Response.ok(aircoachStopService.getStops().blockingGet()).build()
    }

    @GET
    @Path("livedata")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(
        value = "Get Aircoach live data",
        response = AircoachLiveData::class,
        responseContainer = "List"
    )
    fun getLiveData(
        @QueryParam(value = "stopId")
        @ApiParam(required = true)
        stopId: String
    ): Response {
        return Response.ok(aircoachLiveDataService.getLiveData(stopId).blockingGet()).build()
    }

}
