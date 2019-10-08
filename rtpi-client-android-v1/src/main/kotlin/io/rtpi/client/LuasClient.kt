package io.rtpi.client

import io.rtpi.api.LuasLiveData
import io.rtpi.api.LuasStop
import io.rtpi.service.luas.LuasLiveDataService
import io.rtpi.service.luas.LuasStopService
import org.threeten.bp.LocalTime

class LuasClient(
    private val luasStopService: LuasStopService,
    private val luasLiveDataService: LuasLiveDataService
) {

    fun getStops(): List<LuasStop> {
        return luasStopService.getStops()
    }

    fun getLiveData(stopId: String): List<LuasLiveData<LocalTime>> {
        return luasLiveDataService.getLiveData(stopId)
    }

}
