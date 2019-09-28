package ie.dublin.rtpi.client

import ie.dublin.rtpi.api.LuasLiveData
import ie.dublin.rtpi.api.LuasStop
import ie.dublin.rtpi.api.RtpiApi
import io.reactivex.Single

class LuasClient(private val rtpiApi: RtpiApi) {

    fun getStops(): Single<List<LuasStop>> {
        return rtpiApi.getLuasStops()
    }

    fun getLiveData(stopId: String, compact: Boolean): Single<List<LuasLiveData>> {
        return rtpiApi.getLuasLiveData(stopId, compact)
    }

}
