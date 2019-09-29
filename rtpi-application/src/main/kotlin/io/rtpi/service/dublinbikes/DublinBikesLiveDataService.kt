package io.rtpi.service.dublinbikes

import io.rtpi.api.DublinBikesLiveData
import io.rtpi.ktx.validate
import io.rtpi.resource.jcdecaux.JcDecauxApi

class DublinBikesLiveDataService(
    private val jcDecauxApi: JcDecauxApi,
    private val jcDecauxApiKey: String
) {

    fun getLiveData(dockId: String): List<DublinBikesLiveData> {
        val json = jcDecauxApi.station(stationNumber = dockId, contract = "Dublin", apiKey = jcDecauxApiKey)
            .validate()
        return listOf(
            DublinBikesLiveData(
                bikes = json.availableBikes,
                docks = json.availableBikeStands
            )
        )
    }

}
