package io.rtpi.client

import io.reactivex.Single
import io.rtpi.api.ServiceLocation
import io.rtpi.service.dublinbus.DublinBusStopService

class DublinBusStaticDataClient(
    private val dublinBusStopService: DublinBusStopService
) {

    private val cached = mutableListOf<ServiceLocation>()

    fun getStops(): Single<List<ServiceLocation>> =
        if (cached.isEmpty()) {
            dublinBusStopService.getStops().doAfterSuccess { cached.addAll(it) }
        } else {
            Single.just(cached)
        }
}
