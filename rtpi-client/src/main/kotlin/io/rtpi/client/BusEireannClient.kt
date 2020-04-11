package io.rtpi.client

import io.rtpi.api.RtpiApi
import io.reactivex.Single
import io.rtpi.api.LiveData
import io.rtpi.api.ServiceLocation

class BusEireannClient(private val rtpiApi: RtpiApi) {

    fun getStops(): Single<List<ServiceLocation>> {
        return rtpiApi.getBusEireannStops().map { it }
    }

    fun getLiveData(stopId: String): Single<List<LiveData>> {
        return rtpiApi.getBusEireannLiveData(stopId).map { it }
    }
}
