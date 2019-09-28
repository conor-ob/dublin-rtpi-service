package ie.dublin.rtpi.client

import ie.dublin.rtpi.api.IrishRailLiveData
import ie.dublin.rtpi.api.IrishRailStation
import ie.dublin.rtpi.api.RtpiApi
import io.reactivex.Single

class IrishRailClient(private val rtpiApi: RtpiApi) {

    fun getStations(): Single<List<IrishRailStation>> {
        return rtpiApi.getIrishRailStations()
    }

    fun getLiveData(stopId: String, compact: Boolean): Single<List<IrishRailLiveData>> {
        return rtpiApi.getIrishRailLiveData(stopId, compact)
    }

}
