package io.rtpi.client

import io.rtpi.api.AircoachLiveData
import io.rtpi.api.AircoachStop
import io.rtpi.api.RtpiApi
import io.reactivex.Single
import org.threeten.bp.LocalTime

class AircoachClient(private val rtpiApi: RtpiApi) {

    fun getStops(): Single<List<AircoachStop>> {
        return rtpiApi.getAircoachStops()
    }

    fun getLiveData(stopId: String, compact: Boolean): Single<List<AircoachLiveData<LocalTime>>> {
        return rtpiApi.getAircoachLiveData(stopId, compact)
    }

}
