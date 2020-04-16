package io.rtpi.client

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.rtpi.external.aircoach.AircoachStopJson
import io.rtpi.external.aircoach.AircoachWebScraper

class AircoachStaticDataApi(
    private val gson: Gson
) : AircoachWebScraper {

    override fun scrapeStops(): List<AircoachStopJson> =
        gson.fromJson(
            AircoachStaticDataApi::class.java.getResource("/aircoach/stops.json").readText(),
            object : TypeToken<List<AircoachStopJson>>() {}.type
        )
}
