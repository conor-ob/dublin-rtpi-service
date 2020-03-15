package io.rtpi.client

import io.reactivex.Single
import io.rtpi.api.AircoachLiveData
import io.rtpi.api.AircoachStop
import io.rtpi.service.aircoach.AircoachLiveDataService
import io.rtpi.service.aircoach.AircoachStopService

class AircoachClient(
    private val aircoachStopService: AircoachStopService,
    private val aircoachLiveDataService: AircoachLiveDataService
) {

    fun getStops(): Single<List<AircoachStop>> {
        return aircoachStopService.getStops()
    }

    fun getLiveData(stopId: String): Single<List<AircoachLiveData>> {
        return aircoachLiveDataService.getLiveData(stopId)
    }

}
