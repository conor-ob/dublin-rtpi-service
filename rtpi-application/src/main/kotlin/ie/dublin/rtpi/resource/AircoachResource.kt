package ie.dublin.rtpi.resource

import ie.dublin.rtpi.service.aircoach.AircoachLiveDataService
import ie.dublin.rtpi.service.aircoach.AircoachStopService
import javax.ws.rs.DefaultValue
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.QueryParam
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

@Path("aircoach")
class AircoachResource(
    private val aircoachStopService: AircoachStopService,
    private val aircoachLiveDataService: AircoachLiveDataService
) {

    @GET
    @Path("stops")
    @Produces(MediaType.APPLICATION_JSON)
    fun getStops(): Response {
        return Response.ok(aircoachStopService.getStops()).build()
    }

    @GET
    @Path("livedata")
    @Produces(MediaType.APPLICATION_JSON)
    fun getLiveData(
        @QueryParam(value = "stopId") stopId: String,
        @QueryParam(value = "compact") @DefaultValue(value = "false") compact: Boolean
    ): Response {
        return Response.ok(aircoachLiveDataService.getLiveData(stopId, compact)).build()
    }

}
