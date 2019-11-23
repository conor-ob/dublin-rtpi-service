package io.rtpi.service.dublinbikes

import io.reactivex.Single
import io.rtpi.api.DublinBikesLiveData
import io.rtpi.external.jcdecaux.JcDecauxApi

class DublinBikesLiveDataService(
    private val jcDecauxApi: JcDecauxApi
) {

    fun getLiveData(dockId: String, apiKey: String): Single<DublinBikesLiveData> {
        return jcDecauxApi.station(stationNumber = dockId, contract = "Dublin", apiKey = apiKey)
            .map { json ->
                DublinBikesLiveData(
                    bikes = json.availableBikes,
                    docks = json.availableBikeStands
                )
            }
    }
}
