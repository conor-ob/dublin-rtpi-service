package io.rtpi.client

import io.rtpi.api.DublinBikesDock
import io.rtpi.api.RtpiApi
import io.reactivex.Single
import io.rtpi.api.DublinBikesLiveData

class DublinBikesClient(private val rtpiApi: RtpiApi) {

    fun getDocks(apiKey: String): Single<List<DublinBikesDock>> {
        return rtpiApi.getDublinBikesDocks(apiKey)
    }

    fun getLiveData(dockId: String, apiKey: String): Single<DublinBikesLiveData> {
        return rtpiApi.getDublinBikesLiveData(dockId, apiKey)
    }

}
