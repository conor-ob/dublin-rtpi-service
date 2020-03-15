package io.rtpi.resource

import com.google.inject.Inject
import io.rtpi.api.LuasLiveData
import io.rtpi.api.LuasStop
import io.rtpi.service.luas.LuasLiveDataService
import io.rtpi.service.luas.LuasStopService
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
@Path("luas")
class LuasResource @Inject constructor(
    private val luasStopService: LuasStopService,
    private val luasLiveDataService: LuasLiveDataService
) {

    @GET
    @Path("stops")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(
        value = "Get Luas stops",
        response = LuasStop::class,
        responseContainer = "List"
    )
    fun getStops(): Response {
        return Response.ok(luasStopService.getStops().blockingGet()).build()
    }

    @GET
    @Path("livedata")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(
        value = "Get Luas live data",
        response = LuasLiveData::class,
        responseContainer = "List"
    )
    fun getLiveData(
        @QueryParam(value = "stopId")
        @ApiParam(required = true)
        stopId: String
    ): Response {
        return Response.ok(luasLiveDataService.getLiveData(stopId).blockingGet()).build()
    }

}
