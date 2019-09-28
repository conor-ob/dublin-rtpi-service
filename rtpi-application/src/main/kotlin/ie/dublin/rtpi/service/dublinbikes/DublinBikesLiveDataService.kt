package ie.dublin.rtpi.service.dublinbikes

import ie.dublin.rtpi.api.DublinBikesLiveData
import ie.dublin.rtpi.ktx.validate
import ie.dublin.rtpi.resource.jcdecaux.JcDecauxApi

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
