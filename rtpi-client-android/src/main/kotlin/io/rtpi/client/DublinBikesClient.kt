package io.rtpi.client

import io.rtpi.api.DublinBikesDock
import io.rtpi.api.DublinBusLiveData
import io.rtpi.api.RtpiApi
import io.reactivex.Single

class DublinBikesClient(private val rtpiApi: RtpiApi) {

    fun getDocks(): Single<List<DublinBikesDock>> {
        return rtpiApi.getDublinBikesDocks()
    }

    fun getLiveData(stopId: String, compact: Boolean): Single<List<DublinBusLiveData>> {
        return rtpiApi.getDublinBusLiveData(stopId, compact)
    }

}
