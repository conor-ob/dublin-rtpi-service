package io.rtpi.external.rtpi

import com.google.gson.annotations.SerializedName

data class RtpiRealTimeBusInformationResponseJson(
    @SerializedName("errorcode") var errorCode: String? = null,
    @SerializedName("errormessage") var errorMessage: String? = null,
    @SerializedName("numberofresults") var numberOfResults: Int? = null,
    @SerializedName("timestamp") var timestamp: String? = null,
    @SerializedName("results") var results: List<RtpiRealTimeBusInformationJson>? = null
)

data class RtpiRealTimeBusInformationJson(
    @SerializedName("arrivaldatetime") var arrivalDateTime: String? = null,
    @SerializedName("duetime") var dueTime: String? = null,
    @SerializedName("departuredatetime") var departureDateTime: String? = null,
    @SerializedName("departureduetime") var departureDueTime: String? = null,
    @SerializedName("scheduledarrivaldatetime") var scheduledArrivalDateTime: String? = null,
    @SerializedName("scheduleddeparturedatetime") var scheduledDepartureDateTime: String? = null,
    @SerializedName("destination") var destination: String? = null,
    @SerializedName("destinationlocalized") var destinationLocalized: String? = null,
    @SerializedName("origin") var origin: String? = null,
    @SerializedName("originlocalized") var originLocalized: String? = null,
    @SerializedName("direction") var direction: String? = null,
    @SerializedName("operator") var operator: String? = null,
    @SerializedName("operatortype") var operatorType: String? = null,
    @SerializedName("additionalinformation") var additionalInformation: String? = null,
    @SerializedName("lowfloorstatus") var lowFloorStatus: String? = null,
    @SerializedName("route") var route: String? = null,
    @SerializedName("sourcetimestamp") var sourceTimestamp: String? = null,
    @SerializedName("monitored") var monitored: String? = null
)

data class ValidatedRtpiRealTimeBusInformationResponse(
    val timestamp: String,
    val results: List<RtpiRealTimeBusInformationJson>
)

data class ValidatedRtpiRealTimeBusInformation(
    val arrivalDateTime: String
)
