package io.rtpi.client

import io.rtpi.api.RtpiApi
import io.reactivex.Single
import io.rtpi.api.LiveData
import io.rtpi.api.ServiceLocation

class IrishRailClient(private val rtpiApi: RtpiApi) {

    fun getStations(): Single<List<ServiceLocation>> {
        return rtpiApi.getIrishRailStations().map { it }
    }

    fun getLiveData(stationId: String): Single<List<LiveData>> {
        return rtpiApi.getIrishRailLiveData(stationId).map { it }
    }
}
