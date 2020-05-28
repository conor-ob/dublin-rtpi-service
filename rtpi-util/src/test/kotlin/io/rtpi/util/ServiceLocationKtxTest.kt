package io.rtpi.util

import io.rtpi.api.Service
import io.rtpi.test.fixtures.createDockLocation
import io.rtpi.test.fixtures.createStopLocation
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class ServiceLocationKtxTest {

    @Test
    fun `get directions should return as expected`() {
        // arrange
        val aircoachStop = createStopLocation(service = Service.AIRCOACH)
        val busEireannStop = createStopLocation(service = Service.BUS_EIREANN)
        val dublinBikesDock = createDockLocation(service = Service.DUBLIN_BIKES)
        val dublinBusStop = createStopLocation(service = Service.DUBLIN_BUS)
        val irishRailStation = createStopLocation(id = "BFAST", service = Service.IRISH_RAIL)
        val dartStation = createStopLocation(id = "CNLLY", service = Service.AIRCOACH)
        val luasStop = createStopLocation(service = Service.LUAS)

        // act
        val aircoachDirections = aircoachStop.directions()
        val busEireannDirections = busEireannStop.directions()
        val dublinBikesDirections = dublinBikesDock.directions()
        val dublinBusDirections = dublinBusStop.directions()
        val irishRailDirections = irishRailStation.directions()
        val dartDirections = dartStation.directions()
        val luasDirections = luasStop.directions()

        // assert
        assertThat(aircoachDirections).isEmpty()
        assertThat(busEireannDirections).isEmpty()
        assertThat(dublinBikesDirections).isEmpty()
        assertThat(dublinBusDirections).isEmpty()
        assertThat(irishRailDirections).isEmpty()
        assertThat(dartDirections).containsExactly("Northbound", "Southbound")
        assertThat(luasDirections).containsExactly("Inbound", "Outbound")
    }
}
