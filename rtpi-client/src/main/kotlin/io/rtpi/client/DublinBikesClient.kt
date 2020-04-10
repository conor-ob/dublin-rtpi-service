package io.rtpi.client

import io.rtpi.api.RtpiApi
import io.reactivex.Single
import io.rtpi.api.LiveData
import io.rtpi.api.ServiceLocation

class DublinBikesClient(private val rtpiApi: RtpiApi) {

    fun getDocks(apiKey: String): Single<List<ServiceLocation>> {
        return rtpiApi.getDublinBikesDocks(apiKey).map { it }
    }

    fun getLiveData(dockId: String, apiKey: String): Single<LiveData> {
        return rtpiApi.getDublinBikesLiveData(dockId, apiKey).map { it }
    }
}
