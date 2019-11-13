package io.rtpi.client

import io.reactivex.Single
import io.rtpi.api.IrishRailLiveData
import io.rtpi.api.IrishRailStation
import io.rtpi.service.irishrail.IrishRailLiveDataService
import io.rtpi.service.irishrail.IrishRailStationService

class IrishRailClient(
    private val irishRailStationService: IrishRailStationService,
    private val irishRailLiveDataService: IrishRailLiveDataService
) {

    fun getStations(): Single<List<IrishRailStation>> {
        return irishRailStationService.getStations()
    }

    fun getLiveData(stationId: String): Single<List<IrishRailLiveData>> {
        return irishRailLiveDataService.getLiveData(stationId)
    }

}
