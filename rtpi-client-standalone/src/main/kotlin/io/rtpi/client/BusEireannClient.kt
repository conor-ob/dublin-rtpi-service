package io.rtpi.client

import io.reactivex.Single
import io.rtpi.api.LiveData
import io.rtpi.api.ServiceLocation
import io.rtpi.service.buseireann.BusEireannStopService
import io.rtpi.service.rtpi.AbstractRtpiLiveDataService

class BusEireannClient(
    private val busEireannStopService: BusEireannStopService,
    private val busEireannLiveDataService: AbstractRtpiLiveDataService
) {

    fun getStops(): Single<List<ServiceLocation>> {
        return busEireannStopService.getStops()
    }

    fun getLiveData(stopId: String): Single<List<LiveData>> {
        return busEireannLiveDataService.getLiveData(stopId)
    }
}
