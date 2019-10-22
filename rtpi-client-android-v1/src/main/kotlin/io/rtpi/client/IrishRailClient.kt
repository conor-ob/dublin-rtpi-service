package io.rtpi.client

import io.rtpi.api.IrishRailLiveData
import io.rtpi.api.IrishRailStation
import io.rtpi.service.irishrail.IrishRailLiveDataService
import io.rtpi.service.irishrail.IrishRailStationService
import org.threeten.bp.LocalTime

class IrishRailClient(
    private val irishRailStationService: IrishRailStationService,
    private val irishRailLiveDataService: IrishRailLiveDataService
) {

    fun getStations(): List<IrishRailStation> {
        return irishRailStationService.getStations()
    }

    fun getLiveData(stationId: String): List<IrishRailLiveData> {
        return irishRailLiveDataService.getLiveData(stationId)
    }

}
