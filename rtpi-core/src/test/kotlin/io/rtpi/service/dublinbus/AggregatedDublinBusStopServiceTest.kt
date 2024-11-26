package io.rtpi.service.dublinbus

import io.mockk.every
import io.mockk.mockk
import io.reactivex.Single
import io.rtpi.api.Coordinate
import io.rtpi.api.Operator
import io.rtpi.api.RouteGroup
import io.rtpi.api.Service
import io.rtpi.external.dublinbus.DublinBusApi
import io.rtpi.external.rtpi.RtpiApi
import io.rtpi.external.rtpi.createRtpiBusStopInformationJson
import io.rtpi.external.rtpi.createRtpiBusStopInformationResponseJson
import io.rtpi.external.rtpi.createRtpiBusStopOperatorInformationJson
import io.rtpi.test.fixtures.createStopLocation
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test

class AggregatedDublinBusStopServiceTest {

    private val dublinBusApi = mockk<DublinBusApi>()
    private val rtpiApi = mockk<RtpiApi>()
    private val dublinBusStopService = DublinBusStopService(dublinBusApi, rtpiApi, rtpiApi)
    private val defaultDublinBusStop = createRtpiBusStopInformationJson(
        stopId = "444",
        fullName = "Kilmacud Rd",
        latitude = "53.28813639",
        longitude = "-6.207540556",
        operators = listOf(
            createRtpiBusStopOperatorInformationJson(
                name = "BAC",
                routes = listOf("11", "116", "47")
            )
        )
    )
    private val defaultGoAheadStop = createRtpiBusStopInformationJson(
        stopId = "444",
        fullName = "Kilmacud Rd",
        latitude = "53.28813639",
        longitude = "-6.207540556",
        operators = listOf(
            createRtpiBusStopOperatorInformationJson(
                name = "GAD",
                routes = listOf("75", "75A")
            )
        )
    )

    @Before
    fun setup() {
        every { rtpiApi.busStopInformation(any(), eq("json")) } returns Single.just(
            createRtpiBusStopInformationResponseJson(
                results = emptyList()
            )
        )
    }

    @Test
    fun `stops without operators will be returned with no operators or routes`() {
        // arrange
        every { rtpiApi.busStopInformation(eq(Operator.DUBLIN_BUS.shortName), eq("json")) } returns Single.just(
            createRtpiBusStopInformationResponseJson(
                results = listOf(
                    defaultDublinBusStop.copy(operators = null)
                )
            )
        )

        // act
        val dublinBusStops = dublinBusStopService.getStops().blockingGet()

        // assert
        assertThat(dublinBusStops).containsExactly(
            createStopLocation(
                id = "444",
                name = "Kilmacud Rd",
                service = Service.DUBLIN_BUS,
                coordinate = Coordinate(53.28813639, -6.207540556),
                routeGroups = emptyList()
            )
        )
    }

    @Test
    fun `stops serviced by multiple operators should be aggregated`() {
        // arrange
        every { rtpiApi.busStopInformation(eq(Operator.DUBLIN_BUS.shortName), eq("json")) } returns Single.just(
            createRtpiBusStopInformationResponseJson(
                results = listOf(defaultDublinBusStop)
            )
        )
        every { rtpiApi.busStopInformation(eq(Operator.GO_AHEAD.shortName), eq("json")) } returns Single.just(
            createRtpiBusStopInformationResponseJson(
                results = listOf(defaultGoAheadStop)
            )
        )

        // act
        val dublinBusStops = dublinBusStopService.getStops().blockingGet()

        // assert
        assertThat(dublinBusStops).containsExactly(
            createStopLocation(
                id = "444",
                name = "Kilmacud Rd",
                service = Service.DUBLIN_BUS,
                coordinate = Coordinate(53.28813639, -6.207540556),
                routeGroups = listOf(
                    RouteGroup(routes = listOf("11", "47", "116"), operator = Operator.DUBLIN_BUS),
                    RouteGroup(routes = listOf("75", "75A"), operator = Operator.GO_AHEAD)
                )
            )
        )
    }
}
