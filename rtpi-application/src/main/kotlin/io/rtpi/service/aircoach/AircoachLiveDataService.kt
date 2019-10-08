package io.rtpi.service.aircoach

import io.rtpi.api.Time
import io.rtpi.resource.aircoach.AircoachApi
import io.rtpi.resource.aircoach.EtaJson
import io.rtpi.resource.aircoach.TimestampJson
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

class AircoachLiveDataService(aircoachApi: AircoachApi) : AbstractAircoachLiveDataService<LocalTime>(aircoachApi) {

    override fun createDueTime(expected: EtaJson?, scheduled: TimestampJson): Time {
        val currentInstant = LocalTime.now()
        if (expected == null) {
            val scheduledInstant = LocalDateTime.parse(scheduled.dateTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
            val minutes = ChronoUnit.MINUTES.between(currentInstant, scheduledInstant).toInt()
            val localTime = scheduledInstant.toLocalTime()
            return Time(minutes)
//            return Time(minutes, localTime.hour, localTime.minute)
        }
        val expectedInstant = LocalDateTime.parse(expected.etaArrive.dateTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        val minutes = ChronoUnit.MINUTES.between(currentInstant, expectedInstant).toInt()
        val localTime = expectedInstant.toLocalTime()
        return Time(minutes)
//        return Time(minutes, localTime.hour, localTime.minute)
    }

}
