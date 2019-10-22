package io.rtpi.service.dublinbikes

import io.rtpi.api.DublinBikesLiveData
import io.rtpi.ktx.validate
import io.rtpi.resource.jcdecaux.JcDecauxApi

class DublinBikesLiveDataService(
    private val jcDecauxApi: JcDecauxApi
) {

    fun getLiveData(dockId: String, apiKey: String): DublinBikesLiveData {
        val json = jcDecauxApi.station(stationNumber = dockId, contract = "Dublin", apiKey = apiKey)
            .validate()
        return DublinBikesLiveData(
                bikes = json.availableBikes,
                docks = json.availableBikeStands
            )
    }

}
