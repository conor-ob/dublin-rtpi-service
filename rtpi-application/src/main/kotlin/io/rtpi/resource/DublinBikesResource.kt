package io.rtpi.resource

import com.google.inject.Inject
import io.rtpi.service.dublinbikes.DublinBikesDockService
import io.rtpi.service.dublinbikes.DublinBikesLiveDataService
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.QueryParam
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

@Path("dublinbikes")
class DublinBikesResource @Inject constructor(
    private val dublinBikesDockService: DublinBikesDockService,
    private val dublinBikesLiveDataService: DublinBikesLiveDataService
) {

    @GET
    @Path("docks")
    @Produces(MediaType.APPLICATION_JSON)
    fun getStops(@QueryParam(value = "apiKey") apiKey: String): Response {
        return Response.ok(dublinBikesDockService.getDocks(apiKey).blockingGet()).build()
    }

    @GET
    @Path("livedata")
    @Produces(MediaType.APPLICATION_JSON)
    fun getLiveData(
        @QueryParam(value = "dockId") dockId: String,
        @QueryParam(value = "apiKey") apiKey: String
    ): Response {
        return Response.ok(dublinBikesLiveDataService.getLiveData(dockId, apiKey).blockingGet()).build()
    }

}
