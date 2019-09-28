package ie.dublin.rtpi.client

import ie.dublin.rtpi.api.DublinBusLiveData
import ie.dublin.rtpi.api.DublinBusStop
import ie.dublin.rtpi.api.RtpiApi
import io.reactivex.Single

class DublinBusClient(private val rtpiApi: RtpiApi) {

    fun getStops(): Single<List<DublinBusStop>> {
        return rtpiApi.getDublinBusStops()
    }

    fun getLiveData(stopId: String, compact: Boolean): Single<List<DublinBusLiveData>> {
        return rtpiApi.getDublinBusLiveData(stopId, compact)
    }

}
