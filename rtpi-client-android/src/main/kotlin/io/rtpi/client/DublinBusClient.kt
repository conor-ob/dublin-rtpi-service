package io.rtpi.client

import io.rtpi.api.DublinBusLiveData
import io.rtpi.api.DublinBusStop
import io.rtpi.api.RtpiApi
import io.reactivex.Single

class DublinBusClient(private val rtpiApi: RtpiApi) {

    fun getStops(): Single<List<DublinBusStop>> {
        return rtpiApi.getDublinBusStops()
    }

    fun getLiveData(stopId: String, compact: Boolean): Single<List<DublinBusLiveData>> {
        return rtpiApi.getDublinBusLiveData(stopId, compact)
    }

}
