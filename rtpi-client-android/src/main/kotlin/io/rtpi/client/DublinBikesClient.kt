package io.rtpi.client

import io.rtpi.api.DublinBikesDock
import io.rtpi.api.DublinBusLiveData
import io.rtpi.api.RtpiApi
import io.reactivex.Single
import org.threeten.bp.LocalTime

class DublinBikesClient(private val rtpiApi: RtpiApi) {

    fun getDocks(): Single<List<DublinBikesDock>> {
        return rtpiApi.getDublinBikesDocks()
    }

    fun getLiveData(stopId: String, compact: Boolean): Single<List<DublinBusLiveData<LocalTime>>> {
        return rtpiApi.getDublinBusLiveData(stopId, compact)
    }

}
