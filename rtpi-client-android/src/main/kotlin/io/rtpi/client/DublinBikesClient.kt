package io.rtpi.client

import io.rtpi.api.DublinBikesDock
import io.rtpi.api.RtpiApi
import io.reactivex.Single
import io.rtpi.api.DublinBikesLiveData

class DublinBikesClient(private val rtpiApi: RtpiApi) {

    fun getDocks(): Single<List<DublinBikesDock>> {
        return rtpiApi.getDublinBikesDocks()
    }

    fun getLiveData(stopId: String): Single<List<DublinBikesLiveData>> {
        return rtpiApi.getDublinBikesLiveData(stopId)
    }

}
