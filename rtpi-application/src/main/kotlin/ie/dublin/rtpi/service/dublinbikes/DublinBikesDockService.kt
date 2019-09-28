package ie.dublin.rtpi.service.dublinbikes

import ie.dublin.rtpi.api.Coordinate
import ie.dublin.rtpi.api.DublinBikesDock
import ie.dublin.rtpi.api.Operator
import ie.dublin.rtpi.ktx.validate
import ie.dublin.rtpi.resource.jcdecaux.JcDecauxApi

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
