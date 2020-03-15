package io.rtpi.resource

import com.google.inject.Inject
import io.rtpi.api.AircoachStop
import io.rtpi.api.BusEireannStop
import io.rtpi.api.DublinBikesDock
import io.rtpi.api.DublinBusStop
import io.rtpi.api.IrishRailStation
import io.rtpi.api.LuasStop
import io.rtpi.service.aircoach.AircoachStopService
import io.rtpi.service.buseireann.BusEireannStopService
import io.rtpi.service.dublinbikes.DublinBikesDockService
import io.rtpi.service.dublinbus.DublinBusStopService
import io.rtpi.service.irishrail.IrishRailStationService
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

@Api(tags = ["Locations"])
@Path("locations")
class ServiceLocationResource @Inject constructor(
    private val aircoachStopService: AircoachStopService,
    private val busEireannStopService: BusEireannStopService,
    private val dublinBikesDockService: DublinBikesDockService,
    private val dublinBusStopService: DublinBusStopService,
    private val irishRailStationService: IrishRailStationService,
    private val luasStopService: LuasStopService
) {

    @GET
    @Path("aircoach")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(
        value = "Get Aircoach stops",
        notes = "Gets locations by scraping the <a href=\"https://tracker.aircoach.ie/stop-finder/\">Aircoach Stop Finder</a>",
        response = AircoachStop::class,
        responseContainer = "List"
    )
    fun getAircoachStops(): Response {
        return Response.ok(aircoachStopService.getStops().blockingGet()).build()
    }

    @GET
    @Path("buseireann")
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
    @Path("dublinbikes")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(
        value = "Get Dublin Bikes docks",
        notes = "Gets locations using the <a href=\"https://developer.jcdecaux.com/#/home/\">JCDecaux API</a>",
        response = DublinBikesDock::class,
        responseContainer = "List"
    )
    fun getDublinBikesDocks(
        @QueryParam(value = "apiKey")
        @ApiParam(required = true)
        apiKey: String
    ): Response {
        return Response.ok(dublinBikesDockService.getDocks(apiKey).blockingGet()).build()
    }

    @GET
    @Path("dublinbus")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(
        value = "Get Dublin Bus stops",
        notes = "Gets locations using the <a href=\"https://data.smartdublin.ie/dataset/real-time-passenger-information-rtpi-for-dublin-bus-bus-eireann-luas-and-irish-rail/\">RTPI API</a>",
        response = DublinBusStop::class,
        responseContainer = "List"
    )
    fun getDublinBusStops(): Response {
        return Response.ok(dublinBusStopService.getStops().blockingGet()).build()
    }

    @GET
    @Path("irishrail")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(
        value = "Get Irish Rail stations",
        notes = "Gets locations using the <a href=\"http://api.irishrail.ie/realtime/\">Irish Rail API</a>",
        response = IrishRailStation::class,
        responseContainer = "List"
    )
    fun getIrishRailStations(): Response {
        return Response.ok(irishRailStationService.getStations().blockingGet()).build()
    }

    @GET
    @Path("luas")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(
        value = "Get Luas stops",
        notes = "Gets locations using the <a href=\"https://data.smartdublin.ie/dataset/real-time-passenger-information-rtpi-for-dublin-bus-bus-eireann-luas-and-irish-rail/\">RTPI API</a>",
        response = LuasStop::class,
        responseContainer = "List"
    )
    fun getLuasStops(): Response {
        return Response.ok(luasStopService.getStops().blockingGet()).build()
    }
}
