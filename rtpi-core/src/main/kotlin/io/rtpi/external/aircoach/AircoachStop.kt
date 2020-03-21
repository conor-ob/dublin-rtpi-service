package io.rtpi.external.aircoach

import com.google.gson.annotations.SerializedName

data class AircoachStopJson(
    @SerializedName("id") var id: String? = null,
    @SerializedName("stopId") var stopId: String? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("shortName") var shortName: String? = null,
    @SerializedName("linkName") var linkName: String? = null,
    @SerializedName("ticketName") var ticketName: String? = null,
    @SerializedName("place") var place: String? = null,
    @SerializedName("stopLatitude") var stopLatitude: Double? = null,
    @SerializedName("stopLongitude") var stopLongitude: Double? = null,
    @SerializedName("services") var services: List<AircoachStopServiceJson>? = null
)

data class AircoachStopServiceJson(
    @SerializedName("route") var route: String? = null,
    @SerializedName("dir") var dir: String? = null,
    @SerializedName("linkName") var linkName: String? = null
)
