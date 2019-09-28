package ie.dublin.rtpi.resource

import ie.dublin.rtpi.service.irishrail.IrishRailLiveDataService
import ie.dublin.rtpi.service.irishrail.IrishRailStationService
import javax.ws.rs.DefaultValue
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.QueryParam
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

@Path("irishrail")
class IrishRailResource(
    private val irishRailStationService: IrishRailStationService,
    private val irishRailLiveDataService: IrishRailLiveDataService
) {

    @GET
    @Path("stations")
    @Produces(MediaType.APPLICATION_JSON)
    fun getStops(): Response {
        return Response.ok(irishRailStationService.getStations()).build()
    }

    @GET
    @Path("livedata")
    @Produces(MediaType.APPLICATION_JSON)
    fun getLiveData(
        @QueryParam(value = "stationId") stationId: String,
        @QueryParam(value = "compact") @DefaultValue(value = "false") compact: Boolean
    ): Response {
        return Response.ok(irishRailLiveDataService.getLiveData(stationId, compact)).build()
    }

}
