package io.rtpi.util

import io.rtpi.api.LiveData
import io.rtpi.api.Operator
import io.rtpi.test.fixtures.createDockLiveData
import io.rtpi.test.fixtures.createPredictionLiveData
import io.rtpi.test.fixtures.createRouteInfo
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class LiveDataGrouperTest {

    @Test
    fun `should return empty list if input is empty`() {
        // arrange
        val liveData = emptyList<LiveData>()

        // act
        val groupedLiveData = LiveDataGrouper.groupLiveData(liveData)

        // assert
        assertThat(groupedLiveData).isEmpty()
    }

    @Test
    fun `dock live data should not be grouped`() {
        // arrange
        val liveData = listOf(
            createDockLiveData(),
            createDockLiveData(),
            createDockLiveData(),
            createDockLiveData()
        )

        // act
        val groupedLiveData = LiveDataGrouper.groupLiveData(liveData)

        // assert
        assertThat(groupedLiveData).isEqualTo(listOf(liveData))
    }

    @Test
    fun `identical routes should be grouped`() {
        // arrange
        val liveData = listOf(
            createPredictionLiveData(
                operator = Operator.DUBLIN_BUS,
                routeInfo = createRouteInfo(
                    route = "7",
                    destination = "Brides Glen"
                )
            ),
            createPredictionLiveData(
                operator = Operator.GO_AHEAD,
                routeInfo = createRouteInfo(
                    route = "17",
                    destination = "Blackrock"
                )
            ),
            createPredictionLiveData(
                operator = Operator.DUBLIN_BUS,
                routeInfo = createRouteInfo(
                    route = "7A",
                    destination = "Loughlinstown Pk"
                )
            ),
            createPredictionLiveData(
                operator = Operator.DUBLIN_BUS,
                routeInfo = createRouteInfo(
                    route = "7",
                    destination = "Brides Glen"
                )
            ),
            createPredictionLiveData(
                operator = Operator.GO_AHEAD,
                routeInfo = createRouteInfo(
                    route = "17",
                    destination = "Blackrock"
                )
            )
        )

        // act
        val groupedLiveData = LiveDataGrouper.groupLiveData(liveData)

        // assert
        assertThat(groupedLiveData.size).isEqualTo(3)
    }

    @Test
    fun `should return empty list if live data types are mixed`() {
        // arrange
        val liveData = listOf(
            createPredictionLiveData(),
            createDockLiveData(),
            createDockLiveData(),
            createPredictionLiveData(),
            createPredictionLiveData()
        )

        // act
        val groupedLiveData = LiveDataGrouper.groupLiveData(liveData)

        // assert
        assertThat(groupedLiveData).isEmpty()
    }
}
