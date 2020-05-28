package io.rtpi.test.client

import io.reactivex.Single
import io.rtpi.api.ServiceLocation
import io.rtpi.service.aircoach.AircoachStopService

class AircoachStaticDataClient(
    private val aircoachStopService: AircoachStopService
) {

    private val cached = mutableListOf<ServiceLocation>()

    fun getStops(): Single<List<ServiceLocation>> =
        if (cached.isEmpty()) {
            aircoachStopService.getStops().doAfterSuccess { cached.addAll(it) }
        } else {
            Single.just(cached)
        }
}
