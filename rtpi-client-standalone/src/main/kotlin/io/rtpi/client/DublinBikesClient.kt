package io.rtpi.client

import io.reactivex.Single
import io.rtpi.api.LiveData
import io.rtpi.api.ServiceLocation
import io.rtpi.service.dublinbikes.DublinBikesDockService
import io.rtpi.service.dublinbikes.DublinBikesLiveDataService

class DublinBikesClient(
    private val dublinBikesDockService: DublinBikesDockService,
    private val dublinBikesLiveDataService: DublinBikesLiveDataService
) {

    fun getDocks(apiKey: String): Single<List<ServiceLocation>> {
        return dublinBikesDockService.getDocks(apiKey)
    }

    fun getLiveData(dockId: String, apiKey: String): Single<LiveData> {
        return dublinBikesLiveDataService.getLiveData(dockId, apiKey)
    }

}
