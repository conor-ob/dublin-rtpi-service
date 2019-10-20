package io.rtpi.service
//
//import io.rtpi.api.TimedLiveData
//
//abstract class AbstractLiveDataService<T> {
//
//    protected fun <T : TimedLiveData> compact(liveData: List<T>): List<T> {
//        val condensedLiveData = LinkedHashMap<Int, T>()
//        for (data in liveData) {
//            val id = id(data)
//            var cachedLiveData = condensedLiveData[id]
//            if (cachedLiveData == null) {
//                condensedLiveData[id] = data
//            } else {
//                val dueTimes = cachedLiveData.liveTimes.toMutableList()
//                dueTimes.add(data.liveTimes.first())
//                cachedLiveData = cachedLiveData.copy(liveTimes = dueTimes)
//                condensedLiveData[id] = cachedLiveData
//            }
//        }
//        return condensedLiveData.values.toList()
//    }
//
//    protected abstract fun <T : TimedLiveData> id(liveData: T): Int
//
//}
