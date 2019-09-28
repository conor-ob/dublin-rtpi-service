package ie.dublin.rtpi.client

import ie.dublin.rtpi.api.AircoachLiveData
import ie.dublin.rtpi.api.AircoachStop
import ie.dublin.rtpi.api.RtpiApi
import io.reactivex.Single

class AircoachClient(private val rtpiApi: RtpiApi) {

    fun getStops(): Single<List<AircoachStop>> {
        return rtpiApi.getAircoachStops()
    }

    fun getLiveData(stopId: String, compact: Boolean): Single<List<AircoachLiveData>> {
        return rtpiApi.getAircoachLiveData(stopId, compact)
    }

}
