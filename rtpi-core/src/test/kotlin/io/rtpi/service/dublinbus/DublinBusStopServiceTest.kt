package io.rtpi.service.dublinbus

import io.mockk.every
import io.mockk.mockk
import io.reactivex.Single
import io.rtpi.api.Coordinate
import io.rtpi.api.Operator
import io.rtpi.api.Route
import io.rtpi.api.createDublinBusStop
import io.rtpi.external.rtpi.RtpiApi
import io.rtpi.external.rtpi.RtpiBusStopInformationJson
import io.rtpi.external.rtpi.RtpiBusStopInformationResponseJson
import io.rtpi.external.rtpi.RtpiBusStopOperatorInformationJson
import org.junit.Test

class DublinBusStopServiceTest {

    @Test
    fun `stops services by multiple operators should be aggregated`() {
        // arrange
        val rtpiApi = mockk<RtpiApi>()
        every { rtpiApi.busStopInformation(eq(Operator.DUBLIN_BUS.shortName), any()) } returns Single.just(
            RtpiBusStopInformationResponseJson(
                results = listOf(
                    RtpiBusStopInformationJson(
                        stopId = "444",
                        fullName = "Kilmacud Rd",
                        latitude = "53.28813639",
                        longitude = "-6.207540556",
                        operators = listOf(
                            RtpiBusStopOperatorInformationJson(
                                name = "BAC",
                                routes = listOf("11", "116", "47")
                            )
                        )
                    )
                )
            )
        )
        every { rtpiApi.busStopInformation(eq(Operator.GO_AHEAD.shortName), any()) } returns Single.just(
            RtpiBusStopInformationResponseJson(
                results = listOf(
                    RtpiBusStopInformationJson(
                        stopId = "444",
                        fullName = "Kilmacud Rd",
                        latitude = "53.28813639",
                        longitude = "-6.207540556",
                        operators = listOf(
                            RtpiBusStopOperatorInformationJson(
                                name = "GAD",
                                routes = listOf("75", "75A")
                            )
                        )
                    )
                )
            )
        )
        val dublinBusStopService = DublinBusStopService(rtpiApi)

        // act
        val observer = dublinBusStopService.getStops().test()

        // assert
        observer.assertValue(
            listOf(
                createDublinBusStop(
                    id = "444",
                    name = "Kilmacud Rd",
                    coordinate = Coordinate(53.28813639, -6.207540556),
                    operators = setOf(Operator.DUBLIN_BUS, Operator.GO_AHEAD),
                    routes = listOf(
                        Route("11", Operator.DUBLIN_BUS),
                        Route("47", Operator.DUBLIN_BUS),
                        Route("75", Operator.GO_AHEAD),
                        Route("75A", Operator.GO_AHEAD),
                        Route("116", Operator.DUBLIN_BUS)
                    )
                )
            )
        )
        observer.dispose()
    }

}
