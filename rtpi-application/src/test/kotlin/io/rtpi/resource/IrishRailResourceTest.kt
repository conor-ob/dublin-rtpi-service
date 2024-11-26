package io.rtpi.resource

import io.mockk.mockk
import io.rtpi.service.irishrail.IrishRailStationService
import org.junit.Test

class IrishRailResourceTest {

    companion object {

//        private val irishRailLiveData = listOf(
//            createIrishRailLiveData(
//                currentTime = LocalTime.of(12, 32),
//                operator = Operator.DART,
//                direction = "Northbound",
//                destination = "Howth",
//                route = "DART"
//            )
//        )
        private val irishRailStationService = mockk<IrishRailStationService>()
//        private val irishRailLiveDataService = mockk<IrishRailLiveDataService> {
//            every { getLiveData(eq("TARA")) } returns Single.just(irishRailLiveData)
//        }

//        @ClassRule @JvmField
//        val resources: ResourceTestRule = ResourceTestRule.builder()
//            .addResource(IrishRailResource(irishRailStationService, irishRailLiveDataService))
//            .build()
    }

    @Test
    fun `Irish Rail resource should return live data`() {
        // act
//        val jsonResponse = resources.target("/irishrail/livedata")
//            .queryParam("stationId", "TARA")
//            .request(MediaType.APPLICATION_JSON)
//            .get(String::class.java)

        // assert

        // TODO
//      val expectedJson = "[\n" +
//            "   {\n" +
//            "      \"route\":\"DART\",\n" +
//            "      \"destination\":\"Howth\",\n" +
//            "      \"nextDueTime\":{\n" +
//            "         \"minutes\":2,\n" +
//            "         \"time\":[\n" +
//            "            12,\n" +
//            "            34\n" +
//            "         ]\n" +
//            "      },\n" +
//            "      \"laterDueTimes\":[\n" +
//            "         {\n" +
//            "            \"minutes\":14,\n" +
//            "            \"time\":[\n" +
//            "               12,\n" +
//            "               46\n" +
//            "            ]\n" +
//            "         },\n" +
//            "         {\n" +
//            "            \"minutes\":27,\n" +
//            "            \"time\":[\n" +
//            "               12,\n" +
//            "               59\n" +
//            "            ]\n" +
//            "         }\n" +
//            "      ],\n" +
//            "      \"operator\":\"DART\",\n" +
//            "      \"direction\":\"Northbound\"\n" +
//            "   }\n" +
//            "]"
//        assertThat(jsonResponse).isEqualTo(expectedJson.replace("\\s+".toRegex(), ""))
    }
}
