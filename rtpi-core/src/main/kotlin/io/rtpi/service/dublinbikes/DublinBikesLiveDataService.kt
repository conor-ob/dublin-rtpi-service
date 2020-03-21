package io.rtpi.service.dublinbikes

import com.google.inject.Inject
import io.reactivex.Single
import io.rtpi.api.DublinBikesLiveData
import io.rtpi.external.jcdecaux.JcDecauxApi
import io.rtpi.external.jcdecaux.StationJson
import io.rtpi.validation.validate

class DublinBikesLiveDataService @Inject constructor(private val jcDecauxApi: JcDecauxApi) {

    fun getLiveData(dockId: String, apiKey: String): Single<DublinBikesLiveData> {
        return jcDecauxApi.station(stationNumber = dockId, contract = "Dublin", apiKey = apiKey)
            .map { validateResponse(it) }
    }

    private fun validateResponse(json: StationJson): DublinBikesLiveData {
        return DublinBikesLiveData(
            bikes = json.availableBikes.validate(),
            docks = json.availableBikeStands.validate()
        )
    }
}
