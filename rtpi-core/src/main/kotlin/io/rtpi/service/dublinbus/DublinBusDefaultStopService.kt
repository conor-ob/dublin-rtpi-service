package io.rtpi.service.dublinbus

import io.reactivex.Single
import io.rtpi.api.Coordinate
import io.rtpi.api.DublinBusStop
import io.rtpi.external.dublinbus.DublinBusApi
import io.rtpi.external.dublinbus.DublinBusDestinationRequestBodyXml
import io.rtpi.external.dublinbus.DublinBusDestinationRequestRootXml
import io.rtpi.external.dublinbus.DublinBusDestinationRequestXml
import io.rtpi.external.dublinbus.DublinBusDestinationResponseXml
import io.rtpi.validation.validate
import io.rtpi.validation.validateStrings

class DublinBusDefaultStopService(private val dublinBusApi: DublinBusApi) {

    private val request = DublinBusDestinationRequestXml(
        DublinBusDestinationRequestBodyXml(
            DublinBusDestinationRequestRootXml()
        )
    )

    fun getStops(): Single<List<DublinBusStop>> {
        return dublinBusApi
            .getAllDestinations(request)
            .map { validateResponse(it) }
    }

    private fun validateResponse(response: DublinBusDestinationResponseXml): List<DublinBusStop> =
        if (response.stops.isNullOrEmpty()) {
            emptyList()
        } else {
            requireNotNull(response.stops)
                .filter { xml ->
                    validateStrings(xml.id, xml.name, xml.latitude, xml.longitude)
                }
                .map { xml ->
                    DublinBusStop(
                        id = xml.id.validate(),
                        name = xml.name.validate(),
                        coordinate = Coordinate(xml.latitude.validate().toDouble(), xml.longitude.validate().toDouble()),
                        operators = emptySet(),
                        routes = emptyList()
                    )
                }
        }
}
