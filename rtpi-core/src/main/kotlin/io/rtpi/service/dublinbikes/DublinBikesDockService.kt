package io.rtpi.service.dublinbikes

import com.google.inject.Inject
import io.reactivex.Single
import io.rtpi.api.Coordinate
import io.rtpi.api.DockLocation
import io.rtpi.api.Service
import io.rtpi.api.ServiceLocation
import io.rtpi.external.jcdecaux.JcDecauxApi
import io.rtpi.external.jcdecaux.StationJson
import io.rtpi.validation.validate
import io.rtpi.validation.validateObjects

class DublinBikesDockService @Inject constructor(private val jcDecauxApi: JcDecauxApi) {

    fun getDocks(apiKey: String): Single<List<ServiceLocation>> {
        return jcDecauxApi.stations(contract = "Dublin", apiKey = apiKey)
            .map { validateResponse(it) }
    }

    private fun validateResponse(response: List<StationJson>): List<ServiceLocation> {
        return if (response.isNullOrEmpty()) {
            emptyList()
        } else {
            response
                .filter { json ->
                    validateObjects(
                        json.number, json.address, json.position?.lat, json.position?.lng,
                        json.availableBikes, json.availableBikeStands, json.bikeStands
                    )
                }
                .map { json ->
                    DockLocation(
                        id = json.number.toString(),
                        name = json.address.validate(),
                        service = Service.DUBLIN_BIKES,
                        coordinate = Coordinate(requireNotNull(json.position).lat.validate(), requireNotNull(json.position).lng.validate()),
                        availableBikes = json.availableBikes.validate(),
                        availableDocks = json.availableBikeStands.validate(),
                        totalDocks = json.bikeStands.validate(),
                        properties = mutableMapOf()
                    )
                }
        }
    }
}
