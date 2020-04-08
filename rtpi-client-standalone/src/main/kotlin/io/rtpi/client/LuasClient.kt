package io.rtpi.client

import io.reactivex.Single
import io.rtpi.api.LiveData
import io.rtpi.api.ServiceLocation
import io.rtpi.service.luas.LuasStopService
import io.rtpi.service.rtpi.AbstractRtpiLiveDataService

class LuasClient(
    private val luasStopService: LuasStopService,
    private val luasLiveDataService: AbstractRtpiLiveDataService
) {

    fun getStops(): Single<List<ServiceLocation>> {
        return luasStopService.getStops()
    }

    fun getLiveData(stopId: String): Single<List<LiveData>> {
        return luasLiveDataService.getLiveData(stopId)
    }

}
