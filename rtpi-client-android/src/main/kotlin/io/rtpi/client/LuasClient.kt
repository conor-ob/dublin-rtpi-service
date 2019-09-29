package io.rtpi.client

import io.rtpi.api.LuasLiveData
import io.rtpi.api.LuasStop
import io.rtpi.api.RtpiApi
import io.reactivex.Single

class LuasClient(private val rtpiApi: RtpiApi) {

    fun getStops(): Single<List<LuasStop>> {
        return rtpiApi.getLuasStops()
    }

    fun getLiveData(stopId: String, compact: Boolean): Single<List<LuasLiveData>> {
        return rtpiApi.getLuasLiveData(stopId, compact)
    }

}
