package io.rtpi.resource

import com.google.inject.Inject
import io.rtpi.api.AircoachLiveData
import io.rtpi.api.BusEireannLiveData
import io.rtpi.api.DublinBikesLiveData
import io.rtpi.api.DublinBusLiveData
import io.rtpi.api.IrishRailLiveData
import io.rtpi.api.LuasLiveData
import io.rtpi.service.aircoach.AircoachLiveDataService
import io.rtpi.service.buseireann.BusEireannLiveDataService
import io.rtpi.service.dublinbikes.DublinBikesLiveDataService
import io.rtpi.service.dublinbus.DublinBusLiveDataService
import io.rtpi.service.irishrail.IrishRailLiveDataService
import io.rtpi.service.luas.LuasLiveDataService
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.QueryParam
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

@Api(tags = ["Live Data"])
@Path("livedata")
class LiveDataResource @Inject constructor(
    private val aircoachLiveDataService: AircoachLiveDataService,
    private val busEireannLiveDataService: BusEireannLiveDataService,
    private val dublinBikesLiveDataService: DublinBikesLiveDataService,
    private val dublinBusLiveDataService: DublinBusLiveDataService,
    private val irishRailLiveDataService: IrishRailLiveDataService,
    private val luasLiveDataService: LuasLiveDataService
) {

    @GET
    @Path("aircoach")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(
        value = "Get Aircoach live data",
        notes = "Gets live data using the <a href=\"https://tracker.aircoach.ie/\">Aircoach API</a>",
        response = AircoachLiveData::class,
        responseContainer = "List"
    )
    fun getAircoachLiveData(
        @QueryParam(value = "locationId")
        @ApiParam(required = true)
        locationId: String
    ): Response {
        return Response.ok(aircoachLiveDataService.getLiveData(locationId).blockingGet()).build()
    }

    @GET
    @Path("buseireann")
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

    @GET
    @Path("dublinbikes")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(
        value = "Get Dublin Bikes live data",
        notes = "Gets live data using the <a href=\"https://developer.jcdecaux.com/#/home/\">JCDecaux API</a>",
        response = DublinBikesLiveData::class,
        responseContainer = "List"
    )
    fun getDublinBikesLiveData(
        @QueryParam(value = "locationId")
        @ApiParam(required = true)
        locationId: String,
        @QueryParam(value = "apiKey")
        @ApiParam(required = true)
        apiKey: String
    ): Response {
        return Response.ok(dublinBikesLiveDataService.getLiveData(locationId, apiKey).blockingGet()).build()
    }

    @GET
    @Path("dublinbus")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(
        value = "Get Dublin Bus live data",
        notes = "Gets live data using the <a href=\"https://data.smartdublin.ie/dataset/real-time-passenger-information-rtpi-for-dublin-bus-bus-eireann-luas-and-irish-rail/\">RTPI API</a>",
        response = DublinBusLiveData::class,
        responseContainer = "List"
    )
    fun getDublinBusLiveData(
        @QueryParam(value = "locationId")
        @ApiParam(required = true)
        locationId: String
    ): Response {
        return Response.ok(dublinBusLiveDataService.getLiveData(locationId).blockingGet()).build()
    }

    @GET
    @Path("irishrail")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(
        value = "Get Irish Rail live data",
        notes = "Gets live data using the <a href=\"http://api.irishrail.ie/realtime/\">Irish Rail API</a>",
        response = IrishRailLiveData::class,
        responseContainer = "List"
    )
    fun getIrishRailLiveData(
        @QueryParam(value = "locationId")
        @ApiParam(required = true)
        locationId: String
    ): Response {
        return Response.ok(irishRailLiveDataService.getLiveData(locationId).blockingGet()).build()
    }

    @GET
    @Path("luas")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(
        value = "Get Luas live data",
        notes = "Gets live data using the <a href=\"https://data.smartdublin.ie/dataset/real-time-passenger-information-rtpi-for-dublin-bus-bus-eireann-luas-and-irish-rail/\">RTPI API</a>",
        response = LuasLiveData::class,
        responseContainer = "List"
    )
    fun getLuasLiveData(
        @QueryParam(value = "locationId")
        @ApiParam(required = true)
        locationId: String
    ): Response {
        return Response.ok(luasLiveDataService.getLiveData(locationId).blockingGet()).build()
    }
}
