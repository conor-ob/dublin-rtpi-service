package io.rtpi.service.dublinbikes

import com.google.inject.Inject
import io.reactivex.Single
import io.rtpi.api.Coordinate
import io.rtpi.api.DublinBikesDock
import io.rtpi.api.Operator
import io.rtpi.external.jcdecaux.JcDecauxApi
import io.rtpi.external.jcdecaux.StationJson
import io.rtpi.validation.validate
import io.rtpi.validation.validateObjects

class DublinBikesDockService @Inject constructor(private val jcDecauxApi: JcDecauxApi) {

    fun getDocks(apiKey: String): Single<List<DublinBikesDock>> {
        return jcDecauxApi.stations(contract = "Dublin", apiKey = apiKey)
            .map { validateResponse(it) }
    }

    private fun validateResponse(response: List<StationJson>): List<DublinBikesDock> {
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
                    DublinBikesDock(
                        id = json.number.toString(),
                        name = json.address.validate(),
                        coordinate = Coordinate(requireNotNull(json.position).lat.validate(), requireNotNull(json.position).lng.validate()),
                        operators = setOf(Operator.DUBLIN_BIKES),
                        availableBikes = json.availableBikes.validate(),
                        availableDocks = json.availableBikeStands.validate(),
                        docks = json.bikeStands.validate()
                    )
                }
        }
    }
}
