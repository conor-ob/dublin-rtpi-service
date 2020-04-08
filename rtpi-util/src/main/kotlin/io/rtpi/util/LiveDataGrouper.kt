package io.rtpi.util

import io.rtpi.api.DockLiveData
import io.rtpi.api.LiveData
import io.rtpi.api.PredictionLiveData
import java.util.Objects.hash

object LiveDataGrouper {

    fun groupLiveData(liveData: List<LiveData>): List<List<LiveData>> {
        return when {
            liveData.isNullOrEmpty() -> emptyList()
            liveData.all { it is DockLiveData } -> listOf(liveData)
            liveData.all { it is PredictionLiveData } -> {
                val groupedLiveData = LinkedHashMap<Int, List<LiveData>>()
                for (data in liveData) {
                    data as PredictionLiveData
                    val id = hash(data.operator, data.route.id, data.route.destination)
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
