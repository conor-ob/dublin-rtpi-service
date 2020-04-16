package io.rtpi.client

import io.reactivex.Single
import io.rtpi.api.ServiceLocation
import io.rtpi.service.dublinbikes.DublinBikesDockService

class DublinBikesStaticDataClient(
    private val dublinBikesDockService: DublinBikesDockService
) {

    private val cached = mutableListOf<ServiceLocation>()

    fun getDocks(): Single<List<ServiceLocation>> =
        if (cached.isEmpty()) {
            dublinBikesDockService.getDocks("").doAfterSuccess { cached.addAll(it) }
        } else {
            Single.just(cached)
        }
}
