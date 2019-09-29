package io.rtpi.resource

import io.rtpi.service.dublinbus.DublinBusLiveDataService
import io.rtpi.service.dublinbus.DublinBusStopService
import javax.ws.rs.DefaultValue
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.QueryParam
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

@Path("dublinbus")
class DublinBusResource(
    private val dublinBusStopService: DublinBusStopService,
    private val dublinBusLiveDataService: DublinBusLiveDataService
) {

    @GET
    @Path("stops")
    @Produces(MediaType.APPLICATION_JSON)
    fun getStops(): Response {
        return Response.ok(dublinBusStopService.getStops()).build()
    }

    @GET
    @Path("livedata")
    @Produces(MediaType.APPLICATION_JSON)
    fun getLiveData(
        @QueryParam(value = "stopId") stopId: String,
        @QueryParam(value = "compact") @DefaultValue(value = "false") compact: Boolean
    ): Response {
        return Response.ok(dublinBusLiveDataService.getLiveData(stopId, compact)).build()
    }

}
