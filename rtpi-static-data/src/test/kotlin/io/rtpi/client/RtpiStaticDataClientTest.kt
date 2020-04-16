package io.rtpi.client

import io.rtpi.api.Service
import org.junit.Test

class RtpiStaticDataClientTest {

    private val rtpiStaticDataClient = RtpiStaticDataClient()

    @Test
    fun `test gson`() {
        val aircoachStops = rtpiStaticDataClient.getServiceLocations(Service.AIRCOACH).blockingGet()
        val busEireannStops = rtpiStaticDataClient.getServiceLocations(Service.BUS_EIREANN).blockingGet()
        val dublinBikesDocks = rtpiStaticDataClient.getServiceLocations(Service.DUBLIN_BIKES).blockingGet()
        val dublinBusStops = rtpiStaticDataClient.getServiceLocations(Service.DUBLIN_BUS).blockingGet()
        val irishRailStations = rtpiStaticDataClient.getServiceLocations(Service.IRISH_RAIL).blockingGet()
        val luasStops = rtpiStaticDataClient.getServiceLocations(Service.LUAS).blockingGet()
    }
}
