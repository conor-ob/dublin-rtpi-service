package ie.dublin.rtpi.client

import com.google.gson.Gson
import ie.dublin.rtpi.api.Coordinate
import ie.dublin.rtpi.api.DueTime
import ie.dublin.rtpi.api.LuasLiveData
import ie.dublin.rtpi.api.LuasStop
import ie.dublin.rtpi.api.Operator
import org.junit.Assert
import org.junit.Test
import org.threeten.bp.LocalTime

class GsonTest {

    private val gson = Gson()

    @Test
    fun testDeserializeStop() {
        val serialized = "{\n" +
            "        \"id\": \"LUAS99\",\n" +
            "        \"name\": \"Conor's Stop\",\n" +
            "        \"coordinate\": {\n" +
            "            \"latitude\": 69.69,\n" +
            "            \"longitude\": 4.20\n" +
            "        },\n" +
            "        \"service\": \"LUAS\",\n" +
            "        \"operators\": [\n" +
            "            \"COMMUTER\",\n" +
            "            \"DART\",\n" +
            "            \"INTERCITY\"\n" +
            "        ],\n" +
            "        \"routes\": {\n" +
            "            \"LUAS\": [\n" +
            "                \"Red Line\",\n" +
            "                \"Green Line\"\n" +
            "            ],\n" +
            "            \"DUBLIN_BUS\": [\n" +
            "                \"46A\",\n" +
            "                \"145\",\n" +
            "                \"184\"\n" +
            "            ]\n" +
            "        }\n" +
            "    }"

        val deserialized = gson.fromJson(serialized, LuasStop::class.java)
        val luasStop = LuasStop(
            id = "LUAS99",
            name = "Conor's Stop",
            coordinate = Coordinate(
                latitude = 69.69,
                longitude = 4.20
            ),
            operators = setOf(Operator.COMMUTER, Operator.DART, Operator.INTERCITY),
            routes = mapOf(
                Operator.LUAS to listOf("Red Line", "Green Line"),
                Operator.DUBLIN_BUS to listOf("46A", "145", "184")
            )
        )

        Assert.assertEquals(luasStop, deserialized)
    }

    @Test
    fun testDeserializeLiveData() {
        val serialized = "{\n" +
            "        \"identifier\": 99906535,\n" +
            "        \"nextDueTime\": {\n" +
            "            \"minutes\": 10,\n" +
            "            \"time\": {\n" +
            "                \"hour\": 10,\n" +
            "                \"minute\": 10,\n" +
            "                \"second\": 0,\n" +
            "                \"nano\": 0\n" +
            "            }\n" +
            "        },\n" +
            "        \"laterDueTimes\": [],\n" +
            "        \"operator\": \"LUAS\",\n" +
            "        \"route\": \"Green\",\n" +
            "        \"destination\": \"Sandyford\",\n" +
            "        \"direction\": \"Outbound\"\n" +
            "    }"

        val deserialized = gson.fromJson(serialized, LuasLiveData::class.java)
        val luasLiveData = LuasLiveData(
            nextDueTime = DueTime(
                minutes = 10L,
                time = LocalTime.of(10, 10)
            ),
            laterDueTimes = emptyList(),
            operator = Operator.LUAS,
            route = "Green",
            destination = "Sandyford",
            direction = "Outbound"
        )

        Assert.assertEquals(luasLiveData, deserialized)
    }

}
