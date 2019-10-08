package io.rtpi.service.irishrail

import io.rtpi.api.Time
import io.rtpi.resource.irishrail.IrishRailApi
import org.threeten.bp.LocalTime
import org.threeten.bp.format.DateTimeFormatter

class IrishRailLiveDataService(irishRailApi: IrishRailApi) : AbstractIrishRailLiveDataService(irishRailApi) {

    override fun createDueTime(
        expectedArrivalTimestamp: String,
        dueInMinutes: String,
        queryTime: String
    ): Time {
        if (expectedArrivalTimestamp == "00:00") {
            val now = LocalTime.parse(queryTime, DateTimeFormatter.ofPattern("HH:mm:ss"))
            val expectedTime = now.plusMinutes(dueInMinutes.toLong())
            return Time(dueInMinutes.toInt())
//            return Time(dueInMinutes.toInt(), expectedTime)
        }
        val expectedTime = LocalTime.parse(expectedArrivalTimestamp, DateTimeFormatter.ofPattern("HH:mm"))
        return Time(dueInMinutes.toInt())
//        return Time(dueInMinutes.toInt(), expectedTime)
    }

}
