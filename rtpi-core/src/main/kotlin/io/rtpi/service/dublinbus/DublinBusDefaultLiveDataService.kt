package io.rtpi.service.dublinbus

import com.google.inject.Inject
import io.reactivex.Single
import io.rtpi.api.Operator
import io.rtpi.api.Prediction
import io.rtpi.api.PredictionLiveData
import io.rtpi.api.RouteInfo
import io.rtpi.api.Service
import io.rtpi.external.dublinbus.DublinBusApi
import io.rtpi.external.dublinbus.DublinBusRealTimeStopDataRequestBodyXml
import io.rtpi.external.dublinbus.DublinBusRealTimeStopDataRequestRootXml
import io.rtpi.external.dublinbus.DublinBusRealTimeStopDataRequestXml
import io.rtpi.external.dublinbus.DublinBusRealTimeStopDataResponseXml
import io.rtpi.external.dublinbus.DublinBusRealTimeStopDataXml
import io.rtpi.validation.validate
import io.rtpi.validation.validateStrings
import java.time.Duration
import java.time.OffsetDateTime
import java.time.ZoneId

private val dublin = ZoneId.of("Europe/Dublin")

class DublinBusDefaultLiveDataService @Inject constructor(
    private val dublinBusApi: DublinBusApi
) {

    fun getLiveData(stopId: String): Single<List<PredictionLiveData>> {
        return dublinBusApi
            .getRealTimeStopData(newRequest(stopId))
            .map { validateResponse(it) }
    }

    private fun validateResponse(response: DublinBusRealTimeStopDataResponseXml) =
        if (response.dublinBusRealTimeStopData.isNullOrEmpty()) {
            emptyList()
        } else {
            requireNotNull(response.dublinBusRealTimeStopData)
                .filter { xml ->
                    validateStrings(
                        xml.routeId, xml.operator, xml.destination, xml.direction,
                        xml.expectedTimestamp, xml.scheduledTimestamp, xml.responseTimestamp
                    )
                }
                .map { xml ->
                    PredictionLiveData(
                        routeInfo = RouteInfo(
                            route = xml.routeId.validate(),
                            origin = "N/A",
                            destination = mapDestination(xml),
                            direction = xml.direction.validate()
                        ),
                        operator = Operator.parse(xml.operator.validate()),
                        service = Service.DUBLIN_BUS,
                        prediction = mapLiveTime(xml)
                    )
                }
                .filter { !it.prediction.waitTime.isNegative }
                .sortedBy { it.prediction.waitTime }
        }

    private fun mapDestination(xml: DublinBusRealTimeStopDataXml): String {
        val destination = xml.destination.validate()
        if (destination.contains(" via ")) {
            val parts = destination.split(" via ")
            return parts.first().validate()
        }
        return xml.destination.validate()
    }

    private fun mapLiveTime(xml: DublinBusRealTimeStopDataXml): Prediction {
        val currentTime = OffsetDateTime.parse(xml.responseTimestamp).atZoneSameInstant(dublin)
        val expectedTime = OffsetDateTime.parse(xml.expectedTimestamp).atZoneSameInstant(dublin)
        val scheduledTime = OffsetDateTime.parse(xml.scheduledTimestamp).atZoneSameInstant(dublin)
        return Prediction(
            waitTime = Duration.between(currentTime, expectedTime),
            currentDateTime = currentTime,
            expectedDateTime = expectedTime,
            scheduledDateTime = scheduledTime
        )
    }

    private fun newRequest(stopId: String) = DublinBusRealTimeStopDataRequestXml(
        DublinBusRealTimeStopDataRequestBodyXml(
            DublinBusRealTimeStopDataRequestRootXml(stopId = stopId, forceRefresh = "true")
        )
    )
}
