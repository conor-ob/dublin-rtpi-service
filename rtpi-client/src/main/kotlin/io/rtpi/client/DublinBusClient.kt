package io.rtpi.client

import io.reactivex.Single
import io.rtpi.api.LiveData
import io.rtpi.api.RtpiApi
import io.rtpi.api.ServiceLocation

class DublinBusClient(private val rtpiApi: RtpiApi) {

    fun getStops(): Single<List<ServiceLocation>> {
        return rtpiApi.getDublinBusStops().map { it }
    }

    fun getLiveData(stopId: String): Single<List<LiveData>> {
        return rtpiApi.getDublinBusLiveData(stopId).map { it }
    }
}
