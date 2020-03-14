package io.rtpi.client

import io.rtpi.api.IrishRailLiveData
import io.rtpi.api.IrishRailStation
import io.rtpi.api.RtpiApi
import io.reactivex.Single

class IrishRailClient(private val rtpiApi: RtpiApi) {

    fun getStations(): Single<List<IrishRailStation>> {
        return rtpiApi.getIrishRailStations()
    }

    fun getLiveData(stationId: String): Single<List<IrishRailLiveData>> {
        return rtpiApi.getIrishRailLiveData(stationId)
    }

}
