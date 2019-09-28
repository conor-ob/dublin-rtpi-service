package ie.dublin.rtpi.service.aircoach

import ie.dublin.rtpi.api.AircoachLiveData
import ie.dublin.rtpi.api.DueTime
import ie.dublin.rtpi.ktx.validate
import ie.dublin.rtpi.resource.aircoach.AircoachApi
import ie.dublin.rtpi.resource.aircoach.EtaJson
import ie.dublin.rtpi.resource.aircoach.TimestampJson
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

class AircoachLiveDataService(private val aircoachApi: AircoachApi) {

    fun getLiveData(stopId: String, compact: Boolean): List<AircoachLiveData> {
        return aircoachApi.getLiveData(stopId)
            .validate()
            .services
            .map { json ->
                AircoachLiveData(
                    nextDueTime = mapDueTime(json.eta, json.time.arrive),
                    laterDueTimes = emptyList(),
                    route = json.route,
                    destination = json.arrival
                )
            }
            .filter { it.nextDueTime.minutes > -1 }
            .sortedBy { it.nextDueTime.minutes }
    }

    private fun mapDueTime(expected: EtaJson?, scheduled: TimestampJson): DueTime {
        val currentInstant = LocalTime.now()
        if (expected == null) {
            val scheduledInstant = LocalDateTime.parse(scheduled.dateTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
            val minutes = ChronoUnit.MINUTES.between(currentInstant, scheduledInstant)
            return DueTime(minutes, scheduledInstant.toLocalTime())
        }
        val expectedInstant = LocalDateTime.parse(expected.etaArrive.dateTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        val minutes = ChronoUnit.MINUTES.between(currentInstant, expectedInstant)
        return DueTime(minutes, expectedInstant.toLocalTime())
    }

}
