package io.rtpi.client

import io.reactivex.Single
import io.rtpi.api.DublinBikesDock
import io.rtpi.api.DublinBikesLiveData
import io.rtpi.service.dublinbikes.DublinBikesDockService
import io.rtpi.service.dublinbikes.DublinBikesLiveDataService

class DublinBikesClient(
    private val dublinBikesDockService: DublinBikesDockService,
    private val dublinBikesLiveDataService: DublinBikesLiveDataService
) {

    fun getDocks(apiKey: String): Single<List<DublinBikesDock>> {
        return dublinBikesDockService.getDocks(apiKey)
    }

    fun getLiveData(dockId: String, apiKey: String): Single<DublinBikesLiveData> {
        return dublinBikesLiveDataService.getLiveData(dockId, apiKey)
    }

}
