package io.rtpi.service.dublinbikes

import io.rtpi.api.Coordinate
import io.rtpi.api.DublinBikesDock
import io.rtpi.api.Operator
import io.rtpi.ktx.validate
import io.rtpi.resource.jcdecaux.JcDecauxApi

class DublinBikesDockService(
    private val jcDecauxApi: JcDecauxApi,
    private val jcDecauxApiKey: String
) {

    fun getDocks(): List<DublinBikesDock> {
        return jcDecauxApi.stations(contract = "Dublin", apiKey = jcDecauxApiKey)
            .validate()
            .map { json ->
                DublinBikesDock(
                    id = json.number.toString(),
                    name = json.address,
                    coordinate = Coordinate(json.position.lat, json.position.lng),
                    operators = setOf(Operator.DUBLIN_BIKES),
                    availableBikes = json.availableBikes,
                    availableDocks = json.availableBikeStands,
                    docks = json.bikeStands
                )
            }
    }

}
