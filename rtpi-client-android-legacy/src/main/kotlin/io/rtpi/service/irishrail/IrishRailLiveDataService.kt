package io.rtpi.service.irishrail

import io.rtpi.api.DueTime
import io.rtpi.resource.irishrail.IrishRailApi
import org.threeten.bp.LocalTime
import org.threeten.bp.format.DateTimeFormatter

class IrishRailLiveDataService(irishRailApi: IrishRailApi) : AbstractIrishRailLiveDataService<LocalTime>(irishRailApi) {

    override fun createDueTime(
        expectedArrivalTimestamp: String,
        dueInMinutes: String,
        queryTime: String
    ): DueTime<LocalTime> {
        if (expectedArrivalTimestamp == "00:00") {
            val now = LocalTime.parse(queryTime, DateTimeFormatter.ofPattern("HH:mm:ss"))
            val expectedTime = now.plusMinutes(dueInMinutes.toLong())
            return DueTime(dueInMinutes.toInt(), expectedTime)
        }
        val expectedTime = LocalTime.parse(expectedArrivalTimestamp, DateTimeFormatter.ofPattern("HH:mm"))
        return DueTime(dueInMinutes.toInt(), expectedTime)
    }

}
