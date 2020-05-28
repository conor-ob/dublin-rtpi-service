package io.rtpi.client

// import com.google.gson.Gson
// import io.rtpi.api.Coordinate
// import io.rtpi.api.LiveTime
// import io.rtpi.api.LuasLiveData
// import io.rtpi.api.LuasStop
// import io.rtpi.api.Operator
// import org.junit.Assert
// import org.junit.Test
//
// class GsonTest {
//
//    private val gson = Gson()
//
//    @Test
//    fun testDeserializeStop() {
//        val serialized = "{\n" +
//            "        \"id\": \"LUAS99\",\n" +
//            "        \"name\": \"Conor's Stop\",\n" +
//            "        \"coordinate\": {\n" +
//            "            \"latitude\": 69.69,\n" +
//            "            \"longitude\": 4.20\n" +
//            "        },\n" +
//            "        \"service\": \"LUAS\",\n" +
//            "        \"operators\": [\n" +
//            "            \"COMMUTER\",\n" +
//            "            \"DART\",\n" +
//            "            \"INTERCITY\"\n" +
//            "        ],\n" +
//            "        \"routes\": {\n" +
//            "            \"LUAS\": [\n" +
//            "                \"Red Line\",\n" +
//            "                \"Green Line\"\n" +
//            "            ],\n" +
//            "            \"DUBLIN_BUS\": [\n" +
//            "                \"46A\",\n" +
//            "                \"145\",\n" +
//            "                \"184\"\n" +
//            "            ]\n" +
//            "        }\n" +
//            "    }"
//
//        val deserialized = gson.fromJson(serialized, LuasStop::class.java)
//        val luasStop = LuasStop(
//            id = "LUAS99",
//            name = "Conor's Stop",
//            coordinate = Coordinate(
//                latitude = 69.69,
//                longitude = 4.20
//            ),
//            operators = setOf(Operator.COMMUTER, Operator.DART, Operator.INTERCITY),
//            routes = mapOf(
//                Operator.LUAS to listOf("Red Line", "Green Line"),
//                Operator.DUBLIN_BUS to listOf("46A", "145", "184")
//            )
//        )
//
//        Assert.assertEquals(luasStop, deserialized)
//    }
//
//    @Test
//    fun testDeserializeLiveData() {
//        val serialized = "{\n" +
//            "    \"route\": \"Green\",\n" +
//            "    \"destination\":\"Sandyford\",\n" +
//            "    \"liveTimes\": [\n" +
//            "        {\n" +
//            "            \"waitTimeSeconds\": 10,\n" +
//            "            \"expectedTimestamp\": \"TEST\"\n" +
//            "        }\n" +
//            "     ],\n" +
//            "    \"operator\": \"LUAS\",\n" +
//            "    \"direction\":\"Outbound\"\n" +
//            "}"
//
//        val deserialized = gson.fromJson(serialized, LuasLiveData::class.java)
//        val deserialized: LuasLiveData = gson.fromJson(serialized, object : TypeToken<LuasLiveData<LocalTime>>(){}.type)
//        val deserialized: LuasLiveData<LocalTime> = gson.fromJson(serialized, object : TypeToken<LuasLiveData<LocalTime>>(){}.type)
//        val luasLiveData = LuasLiveData(
//            liveTime = listOf(
//                LiveTime(
//                    waitTimeMinutes = 10,
//                    expectedArrivalTimestamp = "TEST"
//                    time = LocalTime.of(10, 10)
//                )
//            ),
//            operator = Operator.LUAS,
//            route = "Green",
//            destination = "Sandyford",
//            direction = "Outbound"
//        )
//
//        Assert.assertEquals(luasLiveData, deserialized)
//    }
//
// }
