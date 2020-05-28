package io.rtpi.client

import io.reactivex.Single
import io.rtpi.api.LiveData
import io.rtpi.api.ServiceLocation
import io.rtpi.service.irishrail.IrishRailLiveDataService
import io.rtpi.service.irishrail.IrishRailStationService

class IrishRailClient(
    private val irishRailStationService: IrishRailStationService,
    private val irishRailLiveDataService: IrishRailLiveDataService
) {

    fun getStations(): Single<List<ServiceLocation>> {
        return irishRailStationService.getStations()
    }

    fun getLiveData(stationId: String): Single<List<LiveData>> {
        return irishRailLiveDataService.getLiveData(stationId)
    }
}
