package io.rtpi.util

import io.rtpi.api.Operator
import io.rtpi.api.Prediction
import io.rtpi.api.createDublinBusLiveData
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.time.Duration
import java.time.ZonedDateTime

class LiveDataGrouperTest {

    @Test
    fun `identical routes should be grouped`() {
        // arrange
        val liveData = listOf(
            createDublinBusLiveData(
                operator = Operator.DUBLIN_BUS,
                route = "7",
                origin = "Mountjoy Square",
                destination = "Brides Glen",
                direction = "Outbound",
                liveTime = Prediction(
                    waitTime = Duration.parse("PT8M"),
                    currentDateTime = ZonedDateTime.now(),
                    expectedDateTime = ZonedDateTime.now(),
                    scheduledDateTime = ZonedDateTime.now()
                )
            ),
            createDublinBusLiveData(
                operator = Operator.GO_AHEAD,
                route = "17",
                origin = "South Circular Road",
                destination = "Blackrock",
                direction = "Outbound",
                liveTime = Prediction(
                    waitTime = Duration.parse("PT20M"),
                    currentDateTime = ZonedDateTime.now(),
                    expectedDateTime = ZonedDateTime.now(),
                    scheduledDateTime = ZonedDateTime.now()
                )
            ),
            createDublinBusLiveData(
                operator = Operator.DUBLIN_BUS,
                route = "7A",
                origin = "Mountjoy Square",
                destination = "Loughlinstown Pk",
                direction = "Outbound",
                liveTime = Prediction(
                    waitTime = Duration.parse("PT27M"),
                    currentDateTime = ZonedDateTime.now(),
                    expectedDateTime = ZonedDateTime.now(),
                    scheduledDateTime = ZonedDateTime.now()
                )
            ),
            createDublinBusLiveData(
                operator = Operator.DUBLIN_BUS,
                route = "7",
                origin = "Mountjoy Square",
                destination = "Brides Glen",
                direction = "Outbound",
                liveTime = Prediction(
                    waitTime = Duration.parse("PT45M"),
                    currentDateTime = ZonedDateTime.now(),
                    expectedDateTime = ZonedDateTime.now(),
                    scheduledDateTime = ZonedDateTime.now()
                )
            ),
            createDublinBusLiveData(
                operator = Operator.GO_AHEAD,
                route = "17",
                origin = "South Circular Road",
                destination = "Blackrock",
                direction = "Outbound",
                liveTime = Prediction(
                    waitTime = Duration.parse("PT47M"),
                    currentDateTime = ZonedDateTime.now(),
                    expectedDateTime = ZonedDateTime.now(),
                    scheduledDateTime = ZonedDateTime.now()
                )
            )
        )

        // act
        val groupedLiveData = LiveDataGrouper.groupLiveData(liveData)

        // assert
        assertThat(groupedLiveData.size).isEqualTo(3)
    }
}
