package io.rtpi.client

import io.rtpi.api.RtpiApi
import io.reactivex.Single
import io.rtpi.api.LiveData
import io.rtpi.api.ServiceLocation

class LuasClient(private val rtpiApi: RtpiApi) {

    fun getStops(): Single<List<ServiceLocation>> {
        return rtpiApi.getLuasStops().map { it }
    }

    fun getLiveData(stopId: String): Single<List<LiveData>> {
        return rtpiApi.getLuasLiveData(stopId).map { it }
    }
}
