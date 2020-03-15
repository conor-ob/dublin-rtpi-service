package io.rtpi.resource

import com.google.inject.Inject
import io.rtpi.api.DublinBusLiveData
import io.rtpi.api.DublinBusStop
import io.rtpi.service.dublinbus.DublinBusLiveDataService
import io.rtpi.service.dublinbus.DublinBusStopService
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
@Path("dublinbus")
class DublinBusResource @Inject constructor(
    private val dublinBusStopService: DublinBusStopService,
    private val dublinBusLiveDataService: DublinBusLiveDataService
) {

    @GET
    @Path("stops")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(
        value = "Get Dublin Bus stops",
        response = DublinBusStop::class,
        responseContainer = "List"
    )
    fun getStops(): Response {
        return Response.ok(dublinBusStopService.getStops().blockingGet()).build()
    }

    @GET
    @Path("livedata")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(
        value = "Get Dublin Bus live data",
        response = DublinBusLiveData::class,
        responseContainer = "List"
    )
    fun getLiveData(
        @QueryParam(value = "stopId")
        @ApiParam(required = true)
        stopId: String
    ): Response {
        return Response.ok(dublinBusLiveDataService.getLiveData(stopId).blockingGet()).build()
    }

}
