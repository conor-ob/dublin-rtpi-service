package io.rtpi.client

import io.rtpi.api.BusEireannLiveData
import io.rtpi.api.BusEireannStop
import io.rtpi.api.RtpiApi
import io.reactivex.Single
import org.threeten.bp.LocalTime

class BusEireannClient(private val rtpiApi: RtpiApi) {

    fun getStops(): Single<List<BusEireannStop>> {
        return rtpiApi.getBusEireannStops()
    }

    fun getLiveData(stopId: String): Single<List<BusEireannLiveData>> {
        return rtpiApi.getBusEireannLiveData(stopId)
    }

}