package io.rtpi.service.dublinbus

import com.google.inject.Inject
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import io.rtpi.api.DublinBusLiveData
import java.time.Duration
import kotlin.math.absoluteValue

class DublinBusLiveDataService @Inject constructor(
    private val dublinBusDefaultLiveDataService: DublinBusDefaultLiveDataService,
    private val dublinBusRtpiLiveDataService: DublinBusRtpiLiveDataService
) {

    fun getLiveData(stopId: String): Single<List<DublinBusLiveData>> {
        return Single.zip(
            dublinBusDefaultLiveDataService.getLiveData(stopId),
            dublinBusRtpiLiveDataService.getLiveData(stopId),
            BiFunction { defaultLiveData, rtpiLiveData -> resolve(defaultLiveData, rtpiLiveData) }
        )
    }

    private fun resolve(
        defaultLiveData: List<DublinBusLiveData>,
        rtpiLiveData: List<DublinBusLiveData>
    ): List<DublinBusLiveData> {
        val nonDuplicateDefaultLiveData = defaultLiveData.toMutableList()
        for (liveData in rtpiLiveData) {
            val route = liveData.route
            val scheduledTime = liveData.liveTime.scheduledDateTime
            val match = defaultLiveData.find {
                it.route == route &&
                Duration.between(it.liveTime.scheduledDateTime, scheduledTime).seconds.absoluteValue < 60L
            }
            if (match != null) {
                nonDuplicateDefaultLiveData.remove(match)
            }
        }
        return rtpiLiveData.plus(nonDuplicateDefaultLiveData)
            .filter { !it.liveTime.waitTime.isNegative }
            .sortedBy { it.liveTime.waitTime }
    }
}
