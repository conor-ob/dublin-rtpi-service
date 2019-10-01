package io.rtpi.client

import io.rtpi.api.BusEireannLiveData
import io.rtpi.api.BusEireannStop
import io.rtpi.service.buseireann.BusEireannLiveDataService
import io.rtpi.service.buseireann.BusEireannStopService
import org.threeten.bp.LocalTime

class BusEireannClient(
    private val busEireannStopService: BusEireannStopService,
    private val busEireannLiveDataService: BusEireannLiveDataService
) {

    fun getStops(): List<BusEireannStop> {
        return busEireannStopService.getStops()
    }

    fun getLiveData(stopId: String, compact: Boolean): List<BusEireannLiveData<LocalTime>> {
        return busEireannLiveDataService.getLiveData(stopId, compact)
    }

}
