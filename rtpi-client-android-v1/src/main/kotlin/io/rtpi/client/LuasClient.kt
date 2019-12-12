package io.rtpi.client

import io.reactivex.Single
import io.rtpi.api.LuasLiveData
import io.rtpi.api.LuasStop
import io.rtpi.service.luas.LuasLiveDataService
import io.rtpi.service.luas.LuasStopService

class LuasClient(
    private val luasStopService: LuasStopService,
    private val luasLiveDataService: LuasLiveDataService
) {

    fun getStops(): Single<List<LuasStop>> {
        return luasStopService.getStops()
    }

    fun getLiveData(stopId: String): Single<List<LuasLiveData>> {
        return luasLiveDataService.getLiveData(stopId)
    }

}
