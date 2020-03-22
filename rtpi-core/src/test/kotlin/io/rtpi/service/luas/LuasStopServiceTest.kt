package io.rtpi.service.luas

import io.mockk.every
import io.reactivex.Single
import io.rtpi.api.Coordinate
import io.rtpi.api.Operator
import io.rtpi.api.Route
import io.rtpi.api.createLuasStop
import io.rtpi.external.rtpi.RtpiApi
import io.rtpi.external.rtpi.createRtpiBusStopInformationJson
import io.rtpi.external.rtpi.createRtpiBusStopInformationResponseJson
import io.rtpi.external.rtpi.createRtpiBusStopOperatorInformationJson
import io.rtpi.service.rtpi.AbstractRtpiStopService
import io.rtpi.service.rtpi.AbstractRtpiStopServiceTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class LuasStopServiceTest : AbstractRtpiStopServiceTest() {

    override fun getOperator() = Operator.LUAS

    override fun createStopService(rtpiApi: RtpiApi) = LuasStopService(rtpiApi) as AbstractRtpiStopService<*>

    override fun createDefaultStop() = createRtpiBusStopInformationJson(
        stopId = "LUAS24",
        fullName = "St. Stephen's Green",
        latitude = "53.2342324",
        longitude = "-6.123445",
        operators = listOf(
            createRtpiBusStopOperatorInformationJson(
                name = "LUAS",
                routes = listOf("Green")
            )
        )
    )

    @Test
    fun `invalid route names should be filtered out`() {
        // arrange
        every { rtpiApi.busStopInformation(eq(getOperator().shortName), eq("json")) } returns Single.just(
            createRtpiBusStopInformationResponseJson(
                results = listOf(
                    defaultStop.copy(
                        operators = defaultStop.operators?.map {
                            createRtpiBusStopOperatorInformationJson(
                                name = it.name,
                                routes = it.routes?.toMutableList()?.plus(listOf("Luas", "XXX"))
                            )
                        }
                    )
                )
            )
        )

        // act
        val stops = stopService.getStops().blockingGet()

        // assert
        assertThat(stops).containsExactly(
            createLuasStop(
                id = "LUAS24",
                name = "St. Stephen's Green",
                coordinate = Coordinate(latitude = 53.2342324, longitude = -6.123445),
                operators = setOf(Operator.LUAS),
                routes = listOf(
                    Route(id = "Green", operator = Operator.LUAS)
                )
            )
        )
    }
}
