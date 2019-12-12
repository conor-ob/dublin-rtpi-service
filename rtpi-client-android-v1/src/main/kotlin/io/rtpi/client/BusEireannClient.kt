package io.rtpi.client

import io.reactivex.Single
import io.rtpi.api.BusEireannLiveData
import io.rtpi.api.BusEireannStop
import io.rtpi.service.buseireann.BusEireannLiveDataService
import io.rtpi.service.buseireann.BusEireannStopService
import org.threeten.bp.LocalTime

class BusEireannClient(
    private val busEireannStopService: BusEireannStopService,
    private val busEireannLiveDataService: BusEireannLiveDataService
) {

    fun getStops(): Single<List<BusEireannStop>> {
        return busEireannStopService.getStops()
    }

    fun getLiveData(stopId: String): Single<List<BusEireannLiveData>> {
        return busEireannLiveDataService.getLiveData(stopId)
    }

}
