package io.rtpi.service.aircoach

import io.rtpi.api.Time
import io.rtpi.resource.aircoach.AircoachApi
import io.rtpi.resource.aircoach.EtaJson
import io.rtpi.resource.aircoach.TimestampJson
import org.threeten.bp.LocalDateTime
import org.threeten.bp.LocalTime
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.temporal.ChronoUnit

class AircoachLiveDataService(aircoachApi: AircoachApi) : AbstractAircoachLiveDataService<LocalTime>(aircoachApi) {

    override fun createDueTime(expected: EtaJson?, scheduled: TimestampJson): Time {
        val currentInstant = LocalTime.now()
        if (expected == null) {
            val scheduledInstant = LocalDateTime.parse(scheduled.dateTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
            val minutes = ChronoUnit.MINUTES.between(currentInstant, scheduledInstant).toInt()
            return Time(minutes)
//            return Time(minutes, scheduledInstant.toLocalTime())
        }
        val expectedInstant = LocalDateTime.parse(expected.etaArrive.dateTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        val minutes = ChronoUnit.MINUTES.between(currentInstant, expectedInstant).toInt()
        return Time(minutes)
//        return Time(minutes, expectedInstant.toLocalTime())
    }

}
