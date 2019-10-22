package io.rtpi.client

import io.rtpi.api.DublinBusLiveData
import io.rtpi.api.DublinBusStop
import io.rtpi.api.RtpiApi
import io.reactivex.Single
import org.threeten.bp.LocalTime

class DublinBusClient(private val rtpiApi: RtpiApi) {

    fun getStops(): Single<List<DublinBusStop>> {
        return rtpiApi.getDublinBusStops()
    }

    fun getLiveData(stopId: String): Single<List<DublinBusLiveData>> {
        return rtpiApi.getDublinBusLiveData(stopId)
    }

}
