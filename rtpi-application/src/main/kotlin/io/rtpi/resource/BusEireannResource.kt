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

@Api(tags = ["Bus Éireann"])
@Path("buseireann")
class BusEireannResource @Inject constructor(
    private val busEireannStopService: BusEireannStopService,
    private val busEireannLiveDataService: BusEireannLiveDataService
) {

    @GET
    @Path("locations")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(
        value = "Get Bus Éireann stops",
        notes = "Gets locations using the <a href=\"https://data.smartdublin.ie/dataset/real-time-passenger-information-rtpi-for-dublin-bus-bus-eireann-luas-and-irish-rail/\">RTPI API</a>",
        response = BusEireannStop::class,
        responseContainer = "List"
    )
    fun getBusEireannStops(): Response {
        return Response.ok(busEireannStopService.getStops().blockingGet()).build()
    }

    @GET
    @Path("livedata")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(
        value = "Get Bus Éireann live data",
        notes = "Gets live data using the <a href=\"https://data.smartdublin.ie/dataset/real-time-passenger-information-rtpi-for-dublin-bus-bus-eireann-luas-and-irish-rail/\">RTPI API</a>",
        response = BusEireannLiveData::class,
        responseContainer = "List"
    )
    fun getBusEireannLiveData(
        @QueryParam(value = "locationId")
        @ApiParam(required = true)
        locationId: String
    ): Response {
        return Response.ok(busEireannLiveDataService.getLiveData(locationId).blockingGet()).build()
    }
}
