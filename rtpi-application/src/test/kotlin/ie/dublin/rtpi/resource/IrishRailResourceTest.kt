package ie.dublin.rtpi.resource

import ie.dublin.rtpi.api.Operator
import ie.dublin.rtpi.api.createIrishRailLiveData
import ie.dublin.rtpi.service.irishrail.IrishRailLiveDataService
import ie.dublin.rtpi.service.irishrail.IrishRailStationService
import io.dropwizard.testing.junit.ResourceTestRule
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.ClassRule
import org.junit.Test
import java.time.LocalTime
import javax.ws.rs.core.MediaType

class IrishRailResourceTest {

    companion object {

        private val irishRailLiveData = listOf(
            createIrishRailLiveData(
                currentTime = LocalTime.of(12, 32),
                operator = Operator.DART,
                direction = "Northbound",
                destination = "Howth",
                route = "DART"
            )
        )
        private val irishRailStationService = mockk<IrishRailStationService>()
        private val irishRailLiveDataService = mockk<IrishRailLiveDataService> {
            every { getLiveData(eq("TARA"),false) } returns irishRailLiveData
        }

        @ClassRule @JvmField
        val resources: ResourceTestRule = ResourceTestRule.builder()
            .addResource(IrishRailResource(irishRailStationService, irishRailLiveDataService))
            .build()

    }

    @Test
    fun `Irish Rail resource should return live data`() {
        // act
        val jsonResponse = resources.target("/irishrail/livedata")
            .queryParam("stationId", "TARA")
            .request(MediaType.APPLICATION_JSON)
            .get(String::class.java)

        // assert
        val expectedJson = "[\n" +
            "   {\n" +
            "      \"identifier\":2027355157,\n" +
            "      \"nextDueTime\":{\n" +
            "         \"minutes\":2,\n" +
            "         \"time\":[\n" +
            "            12,\n" +
            "            34\n" +
            "         ]\n" +
            "      },\n" +
            "      \"laterDueTimes\":[\n" +
            "         {\n" +
            "            \"minutes\":14,\n" +
            "            \"time\":[\n" +
            "               12,\n" +
            "               46\n" +
            "            ]\n" +
            "         },\n" +
            "         {\n" +
            "            \"minutes\":27,\n" +
            "            \"time\":[\n" +
            "               12,\n" +
            "               59\n" +
            "            ]\n" +
            "         }\n" +
            "      ],\n" +
            "      \"operator\":\"DART\",\n" +
            "      \"route\":\"DART\",\n" +
            "      \"destination\":\"Howth\",\n" +
            "      \"direction\":\"Northbound\"\n" +
            "   }\n" +
            "]"
//        assertThat(jsonResponse).isEqualTo(expectedJson.replace("\\s+".toRegex(), "")) //TODO
    }

}
