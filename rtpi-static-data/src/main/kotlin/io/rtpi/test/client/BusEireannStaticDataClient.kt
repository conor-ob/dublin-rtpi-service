package io.rtpi.test.client

import io.reactivex.Single
import io.rtpi.api.ServiceLocation
import io.rtpi.service.buseireann.BusEireannStopService

class BusEireannStaticDataClient(
    private val busEireannStopService: BusEireannStopService
) {

    private val cached = mutableListOf<ServiceLocation>()

    fun getStops(): Single<List<ServiceLocation>> =
        if (cached.isEmpty()) {
            busEireannStopService.getStops().doAfterSuccess { cached.addAll(it) }
        } else {
            Single.just(cached)
        }
}
