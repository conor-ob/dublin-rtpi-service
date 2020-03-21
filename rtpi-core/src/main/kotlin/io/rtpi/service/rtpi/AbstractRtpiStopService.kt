package io.rtpi.service.rtpi

import io.reactivex.Single
import io.rtpi.api.ServiceLocationRoutes
import io.rtpi.external.rtpi.RtpiApi
import io.rtpi.external.rtpi.RtpiBusStopInformationJson

private const val JSON = "json"

abstract class AbstractRtpiStopService<T : ServiceLocationRoutes>(
    private val rtpiApi: RtpiApi,
    private val operator: String
) {

    fun getStops(): Single<List<T>> {
        return rtpiApi.busStopInformation(
            operator = operator,
            format = JSON
        ).map { response ->
            response.results
                .filter { json ->
                    json.stopId != null
                        && json.fullName != null
                        && json.latitude != null
                        && json.longitude != null
                        && json.operators.isNotEmpty()
                }
                .map { json -> newServiceLocationInstance(response.timestamp!!, json) }
            }
    }

    protected abstract fun newServiceLocationInstance(timestamp: String, json: RtpiBusStopInformationJson): T

}
