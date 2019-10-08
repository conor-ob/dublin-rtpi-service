package io.rtpi.client

import io.rtpi.api.AircoachLiveData
import io.rtpi.api.AircoachStop
import io.rtpi.service.aircoach.AircoachLiveDataService
import io.rtpi.service.aircoach.AircoachStopService
import org.threeten.bp.LocalTime

class AircoachClient(
    private val aircoachStopService: AircoachStopService,
    private val aircoachLiveDataService: AircoachLiveDataService
) {

    fun getStops(): List<AircoachStop> {
        return aircoachStopService.getStops()
    }

    fun getLiveData(stopId: String): List<AircoachLiveData<LocalTime>> {
        return aircoachLiveDataService.getLiveData(stopId)
    }

}
