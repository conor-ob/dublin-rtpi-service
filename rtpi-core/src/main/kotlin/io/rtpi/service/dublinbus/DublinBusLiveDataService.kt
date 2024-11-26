package io.rtpi.service.dublinbus

import com.google.inject.Inject
import com.google.inject.name.Named
import io.reactivex.Single
import io.rtpi.api.LiveData
import io.rtpi.api.PredictionLiveData
import io.rtpi.external.dublinbus.DublinBusApi
import io.rtpi.external.rtpi.RtpiApi
import java.time.Duration
import kotlin.math.absoluteValue

class DublinBusLiveDataService @Inject constructor(
    dublinBusApi: DublinBusApi,
    @Named("rtpi_api") rtpiApi: RtpiApi,
    @Named("rtpi_fallback_api") rtpiFallbackApi: RtpiApi
) {

    private val dublinBusDefaultLiveDataService = DublinBusDefaultLiveDataService(dublinBusApi)
    private val dublinBusRtpiLiveDataService = DublinBusRtpiLiveDataService(rtpiApi, rtpiFallbackApi)

    fun getLiveData(stopId: String): Single<List<LiveData>> {
        return dublinBusRtpiLiveDataService.getLiveData(stopId)
//        return Single.zip(
//            dublinBusDefaultLiveDataService.getLiveData(stopId),
//            dublinBusRtpiLiveDataService.getLiveData(stopId),
//            BiFunction { defaultLiveData, rtpiLiveData -> resolve(defaultLiveData, rtpiLiveData as List<PredictionLiveData>) }
//        )
    }

    private fun resolve(
        defaultLiveData: List<PredictionLiveData>,
        rtpiLiveData: List<PredictionLiveData>
    ): List<LiveData> {
        val nonDuplicateDefaultLiveData = defaultLiveData.toMutableList()
        for (liveData in rtpiLiveData) {
            val route = liveData.routeInfo.route
            val scheduledTime = liveData.prediction.scheduledDateTime
            val match = defaultLiveData.find {
                it.routeInfo.route == route &&
                Duration.between(it.prediction.scheduledDateTime, scheduledTime).seconds.absoluteValue < 60L
            }
            if (match != null) {
                nonDuplicateDefaultLiveData.remove(match)
            }
        }
        return rtpiLiveData.plus(nonDuplicateDefaultLiveData)
            .filter { !it.prediction.waitTime.isNegative }
            .sortedBy { it.prediction.waitTime }
    }
}
