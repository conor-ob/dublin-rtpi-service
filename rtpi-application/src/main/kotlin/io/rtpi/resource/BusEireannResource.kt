package io.rtpi.resource

import com.google.inject.Inject
import io.rtpi.api.PredictionLiveData
import io.rtpi.api.StopLocation
import io.rtpi.service.buseireann.CachedBusEireannLiveDataService
import io.rtpi.service.buseireann.CachedBusEireannStopService
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
    private val busEireannStopService: CachedBusEireannStopService,
    private val busEireannLiveDataService: CachedBusEireannLiveDataService
) {

    @GET
    @Path("locations")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(
        value = "Get Bus Éireann stops",
        notes = "Gets locations using the <a href=\"https://data.smartdublin.ie/dataset/real-time-passenger-information-rtpi-for-dublin-bus-bus-eireann-luas-and-irish-rail/\">RTPI API</a>",
        response = StopLocation::class,
        responseContainer = "List"
    )
    fun getBusEireannStops(): Response {
        return Response.ok(busEireannStopService.getStops()).build()
    }

    @GET
    @Path("livedata")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(
        value = "Get Bus Éireann live data",
        notes = "Gets live data using the <a href=\"https://data.smartdublin.ie/dataset/real-time-passenger-information-rtpi-for-dublin-bus-bus-eireann-luas-and-irish-rail/\">RTPI API</a>",
        response = PredictionLiveData::class,
        responseContainer = "List"
    )
    fun getBusEireannLiveData(
        @QueryParam(value = "locationId")
        @ApiParam(required = true)
        locationId: String
    ): Response {
        return Response.ok(busEireannLiveDataService.getLiveData(locationId)).build()
    }
}
