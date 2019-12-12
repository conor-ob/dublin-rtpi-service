package io.rtpi.client

import io.reactivex.Single
import io.rtpi.api.DublinBusLiveData
import io.rtpi.api.DublinBusStop
import io.rtpi.service.dublinbus.DublinBusLiveDataService
import io.rtpi.service.dublinbus.DublinBusStopService
import org.threeten.bp.LocalTime

class DublinBusClient(
    private val dublinBusStopService: DublinBusStopService,
    private val dublinBusLiveDataService: DublinBusLiveDataService
) {

    fun getStops(): Single<List<DublinBusStop>> {
        return dublinBusStopService.getStops()
    }

    fun getLiveData(stopId: String): Single<List<DublinBusLiveData>> {
        return dublinBusLiveDataService.getLiveData(stopId)
    }

}
