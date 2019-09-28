package ie.dublin.rtpi.client

import ie.dublin.rtpi.api.BusEireannLiveData
import ie.dublin.rtpi.api.BusEireannStop
import ie.dublin.rtpi.api.RtpiApi
import io.reactivex.Single

class BusEireannClient(private val rtpiApi: RtpiApi) {

    fun getStops(): Single<List<BusEireannStop>> {
        return rtpiApi.getBusEireannStops()
    }

    fun getLiveData(stopId: String, compact: Boolean): Single<List<BusEireannLiveData>> {
        return rtpiApi.getBusEireannLiveData(stopId, compact)
    }

}
