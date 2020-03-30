package io.rtpi.util

import io.rtpi.api.DublinBikesLiveData
import io.rtpi.api.LiveData
import io.rtpi.api.TimedLiveData
import java.util.Objects.hash

object LiveDataGrouper {

    fun groupLiveData(liveData: List<LiveData>): List<List<LiveData>> {
        return when {
            liveData.isNullOrEmpty() -> emptyList()
            liveData.all { it is DublinBikesLiveData } -> listOf(liveData)
            liveData.all { it is TimedLiveData } -> {
                val groupedLiveData = LinkedHashMap<Int, List<LiveData>>()
                for (data in liveData) {
                    data as TimedLiveData
                    val id = hash(data.operator, data.route, data.destination)
                    val cachedLiveData = groupedLiveData[id]
                    if (cachedLiveData == null) {
                        groupedLiveData[id] = listOf(data)
                    } else {
                        val updated = cachedLiveData.toMutableList()
                        updated.add(data)
                        groupedLiveData[id] = updated
                    }
                }
                groupedLiveData.map { it.value }
            }
            else -> emptyList()
        }
    }
}
