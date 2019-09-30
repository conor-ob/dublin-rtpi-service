package io.rtpi.client

import io.rtpi.api.DublinBikesDock
import io.rtpi.api.DublinBikesLiveData
import io.rtpi.service.dublinbikes.DublinBikesDockService
import io.rtpi.service.dublinbikes.DublinBikesLiveDataService

class DublinBikesClient(
    private val dublinBikesDockService: DublinBikesDockService,
    private val dublinBikesLiveDataService: DublinBikesLiveDataService
) {

    fun getDocks(): List<DublinBikesDock> {
        return dublinBikesDockService.getDocks()
    }

    fun getLiveData(dockId: String): List<DublinBikesLiveData> {
        return dublinBikesLiveDataService.getLiveData(dockId)
    }

}
