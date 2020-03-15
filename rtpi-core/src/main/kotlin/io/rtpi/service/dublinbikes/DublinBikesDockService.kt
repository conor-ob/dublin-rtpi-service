package io.rtpi.service.dublinbikes

import com.google.inject.Inject
import io.reactivex.Single
import io.rtpi.api.Coordinate
import io.rtpi.api.DublinBikesDock
import io.rtpi.api.Operator
import io.rtpi.external.jcdecaux.JcDecauxApi

class DublinBikesDockService @Inject constructor(private val jcDecauxApi: JcDecauxApi) {

    fun getDocks(apiKey: String): Single<List<DublinBikesDock>> {
        return jcDecauxApi.stations(contract = "Dublin", apiKey = apiKey)
            .map {
                it.map { json ->
                    DublinBikesDock(
                        id = json.number.toString(),
                        name = json.address!!.trim(),
                        coordinate = Coordinate(json.position!!.lat!!, json.position!!.lng!!),
                        operators = setOf(Operator.DUBLIN_BIKES),
                        availableBikes = json.availableBikes!!,
                        availableDocks = json.availableBikeStands!!,
                        docks = json.bikeStands!!
                    )
                }
            }
    }

}
