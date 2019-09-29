package io.rtpi.resource

import io.rtpi.service.luas.LuasLiveDataService
import io.rtpi.service.luas.LuasStopService
import javax.ws.rs.DefaultValue
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.QueryParam
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

@Path("luas")
class LuasResource(
    private val luasStopService: LuasStopService,
    private val luasLiveDataService: LuasLiveDataService
) {

    @GET
    @Path("stops")
    @Produces(MediaType.APPLICATION_JSON)
    fun getStops(): Response {
        return Response.ok(luasStopService.getStops()).build()
    }

    @GET
    @Path("livedata")
    @Produces(MediaType.APPLICATION_JSON)
    fun getLiveData(
        @QueryParam(value = "stopId") stopId: String,
        @QueryParam(value = "compact") @DefaultValue(value = "false") compact: Boolean
    ): Response {
        return Response.ok(luasLiveDataService.getLiveData(stopId, compact)).build()
    }

}
