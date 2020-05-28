package io.rtpi.test.client

import io.reactivex.Single
import io.rtpi.api.ServiceLocation
import io.rtpi.service.luas.LuasStopService

class LuasStaticDataClient(
    private val luasStopService: LuasStopService
) {

    private val cached = mutableListOf<ServiceLocation>()

    fun getStops(): Single<List<ServiceLocation>> =
        if (cached.isEmpty()) {
            luasStopService.getStops().doAfterSuccess { cached.addAll(it) }
        } else {
            Single.just(cached)
        }
}
