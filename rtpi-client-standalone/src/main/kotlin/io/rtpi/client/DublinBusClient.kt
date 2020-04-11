package io.rtpi.client

import io.reactivex.Single
import io.rtpi.api.LiveData
import io.rtpi.api.ServiceLocation
import io.rtpi.service.dublinbus.DublinBusLiveDataService
import io.rtpi.service.dublinbus.DublinBusStopService

class DublinBusClient(
    private val dublinBusStopService: DublinBusStopService,
    private val dublinBusLiveDataService: DublinBusLiveDataService
) {

    fun getStops(): Single<List<ServiceLocation>> {
        return dublinBusStopService.getStops()
    }

    fun getLiveData(stopId: String): Single<List<LiveData>> {
        return dublinBusLiveDataService.getLiveData(stopId)
    }

}
