package io.rtpi.service.rtpi

import io.mockk.every
import io.mockk.mockk
import io.reactivex.Single
import io.rtpi.api.Operator
import io.rtpi.external.rtpi.RtpiApi
import io.rtpi.external.rtpi.RtpiBusStopInformationJson
import io.rtpi.external.rtpi.createRtpiBusStopInformationResponseJson
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test

abstract class RtpiStopServiceTest {

    protected val rtpiApi = mockk<RtpiApi>()
    protected val stopService: AbstractRtpiStopService by lazy { createStopService(rtpiApi) }
    protected val defaultStop: RtpiBusStopInformationJson by lazy { createDefaultStop() }

    protected abstract fun getOperator(): Operator

    protected abstract fun createStopService(rtpiApi: RtpiApi): AbstractRtpiStopService

    protected abstract fun createDefaultStop(): RtpiBusStopInformationJson

    @Before
    fun setup() {
        every { rtpiApi.busStopInformation(any(), eq("json")) } returns Single.just(
            createRtpiBusStopInformationResponseJson(
                results = emptyList()
            )
        )
    }

    @Test
    fun `stops without an id will not be returned`() {
        // arrange
        every { rtpiApi.busStopInformation(eq(getOperator().shortName), eq("json")) } returns Single.just(
            createRtpiBusStopInformationResponseJson(
                results = listOf(
                    defaultStop.copy(stopId = null)
                )
            )
        )

        // act
        val stops = stopService.getStops().blockingGet()

        // assert
        assertThat(stops).isEmpty()
    }

    @Test
    fun `stops without a name will not be returned`() {
        // arrange
        every { rtpiApi.busStopInformation(eq(getOperator().shortName), eq("json")) } returns Single.just(
            createRtpiBusStopInformationResponseJson(
                results = listOf(
                    defaultStop.copy(fullName = null)
                )
            )
        )

        // act
        val stops = stopService.getStops().blockingGet()

        // assert
        assertThat(stops).isEmpty()
    }

    @Test
    fun `stops without a latitude will not be returned`() {
        // arrange
        every { rtpiApi.busStopInformation(eq(getOperator().shortName), eq("json")) } returns Single.just(
            createRtpiBusStopInformationResponseJson(
                results = listOf(
                    defaultStop.copy(latitude = null)
                )
            )
        )

        // act
        val stops = stopService.getStops().blockingGet()

        // assert
        assertThat(stops).isEmpty()
    }

    @Test
    fun `stops without a longitude will not be returned`() {
        // arrange
        every { rtpiApi.busStopInformation(eq(getOperator().shortName), eq("json")) } returns Single.just(
            createRtpiBusStopInformationResponseJson(
                results = listOf(
                    defaultStop.copy(longitude = null)
                )
            )
        )

        // act
        val stops = stopService.getStops().blockingGet()

        // assert
        assertThat(stops).isEmpty()
    }

    @Test
    fun `stops with invalid coordinates will not be returned`() {
        // arrange
        every { rtpiApi.busStopInformation(eq(getOperator().shortName), eq("json")) } returns Single.just(
            createRtpiBusStopInformationResponseJson(
                results = listOf(
                    defaultStop.copy(latitude = "0.0", longitude = "0.0")
                )
            )
        )

        // act
        val stops = stopService.getStops().blockingGet()

        // assert
        assertThat(stops).isEmpty()
    }

    @Test
    fun `null response from API will return empty list`() {
        // arrange
        every { rtpiApi.busStopInformation(eq(getOperator().shortName), eq("json")) } returns Single.just(
            createRtpiBusStopInformationResponseJson(
                results = null
            )
        )

        // act
        val stops = stopService.getStops().blockingGet()

        // assert
        assertThat(stops).isEmpty()
    }
}
