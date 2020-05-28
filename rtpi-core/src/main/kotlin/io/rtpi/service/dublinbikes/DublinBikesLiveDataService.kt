package io.rtpi.service.dublinbikes

import com.google.inject.Inject
import io.reactivex.Single
import io.rtpi.api.DockLiveData
import io.rtpi.api.LiveData
import io.rtpi.api.Operator
import io.rtpi.api.Service
import io.rtpi.external.jcdecaux.JcDecauxApi
import io.rtpi.external.jcdecaux.StationJson
import io.rtpi.validation.validate

class DublinBikesLiveDataService @Inject constructor(private val jcDecauxApi: JcDecauxApi) {

    fun getLiveData(dockId: String, apiKey: String): Single<LiveData> {
        return jcDecauxApi.station(stationNumber = dockId, contract = "Dublin", apiKey = apiKey)
            .map { validateResponse(it) }
    }

    private fun validateResponse(json: StationJson): LiveData {
        return DockLiveData(
            operator = Operator.DUBLIN_BIKES,
            service = Service.DUBLIN_BIKES,
            availableBikes = json.availableBikes.validate(),
            availableDocks = json.availableBikeStands.validate(),
            totalDocks = json.bikeStands.validate()
        )
    }
}
