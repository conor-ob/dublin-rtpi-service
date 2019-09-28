package ie.dublin.rtpi.client

import ie.dublin.rtpi.api.DublinBikesDock
import ie.dublin.rtpi.api.DublinBusLiveData
import ie.dublin.rtpi.api.RtpiApi
import io.reactivex.Single

class DublinBikesClient(private val rtpiApi: RtpiApi) {

    fun getDocks(): Single<List<DublinBikesDock>> {
        return rtpiApi.getDublinBikesDocks()
    }

    fun getLiveData(stopId: String, compact: Boolean): Single<List<DublinBusLiveData>> {
        return rtpiApi.getDublinBusLiveData(stopId, compact)
    }

}
