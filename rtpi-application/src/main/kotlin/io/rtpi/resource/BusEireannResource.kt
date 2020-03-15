package io.rtpi.resource

import com.google.inject.Inject
import io.rtpi.api.BusEireannLiveData
import io.rtpi.api.BusEireannStop
import io.rtpi.service.buseireann.BusEireannLiveDataService
import io.rtpi.service.buseireann.BusEireannStopService
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
@Path("buseireann")
class BusEireannResource @Inject constructor(
    private val busEireannStopService: BusEireannStopService,
    private val busEireannLiveDataService: BusEireannLiveDataService
) {

    @GET
    @Path("stops")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(
        value = "Get Bus Éireann stops",
        response = BusEireannStop::class,
        responseContainer = "List"
    )
    fun getStops(): Response {
        return Response.ok(busEireannStopService.getStops().blockingGet()).build()
    }

    @GET
    @Path("livedata")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(
        value = "Get Bus Éireann live data",
        response = BusEireannLiveData::class,
        responseContainer = "List"
    )
    fun getLiveData(
        @QueryParam(value = "stopId")
        @ApiParam(required = true)
        stopId: String
    ): Response {
        return Response.ok(busEireannLiveDataService.getLiveData(stopId).blockingGet()).build()
    }

}
