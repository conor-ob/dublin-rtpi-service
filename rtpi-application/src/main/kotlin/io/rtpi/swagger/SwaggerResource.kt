package io.rtpi.swagger

import io.dropwizard.views.View
import io.swagger.annotations.Api
import javax.inject.Singleton
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

@Api(hidden = true)
@Singleton
@Path("/")
class SwaggerResource {

    @GET
    @Produces(MediaType.TEXT_HTML)
    @Path("/swagger.html")
    fun index(): View = object : View("swagger.ftl") { }
}
