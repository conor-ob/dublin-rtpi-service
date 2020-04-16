package io.rtpi.client

import io.reactivex.Single
import io.rtpi.api.ServiceLocation
import io.rtpi.service.irishrail.IrishRailStationService

class IrishRailStaticDataClient(
    private val irishRailStationService: IrishRailStationService
) {

    private val cached = mutableListOf<ServiceLocation>()

    fun getStations(): Single<List<ServiceLocation>> =
        if (cached.isEmpty()) {
            irishRailStationService.getStations().doAfterSuccess { cached.addAll(it) }
        } else {
            Single.just(cached)
        }
}
