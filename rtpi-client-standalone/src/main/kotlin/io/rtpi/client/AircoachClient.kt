package io.rtpi.client

import io.reactivex.Single
import io.rtpi.api.LiveData
import io.rtpi.api.ServiceLocation
import io.rtpi.service.aircoach.AircoachLiveDataService
import io.rtpi.service.aircoach.AircoachStopService

class AircoachClient(
    private val aircoachStopService: AircoachStopService,
    private val aircoachLiveDataService: AircoachLiveDataService
) {

    fun getStops(): Single<List<ServiceLocation>> {
        return aircoachStopService.getStops()
    }

    fun getLiveData(stopId: String): Single<List<LiveData>> {
        return aircoachLiveDataService.getLiveData(stopId)
    }

}
