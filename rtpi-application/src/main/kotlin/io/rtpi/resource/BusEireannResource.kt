package io.rtpi.resource

import io.rtpi.service.buseireann.BusEireannLiveDataService
import io.rtpi.service.buseireann.BusEireannStopService
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.QueryParam
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

@Path("buseireann")
class BusEireannResource(
    private val busEireannStopService: BusEireannStopService,
    private val busEireannLiveDataService: BusEireannLiveDataService
) {

    @GET
    @Path("stops")
    @Produces(MediaType.APPLICATION_JSON)
    fun getStops(): Response {
        return Response.ok(busEireannStopService.getStops().blockingGet()).build()
    }

    @GET
    @Path("livedata")
    @Produces(MediaType.APPLICATION_JSON)
    fun getLiveData(@QueryParam(value = "stopId") stopId: String): Response {
        return Response.ok(busEireannLiveDataService.getLiveData(stopId).blockingGet()).build()
    }

}
