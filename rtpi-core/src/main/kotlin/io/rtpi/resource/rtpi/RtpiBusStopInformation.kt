package io.rtpi.resource.rtpi

import com.google.gson.annotations.SerializedName

data class RtpiBusStopInformationResponseJson(
    @SerializedName("errorcode") val errorCode: String,
    @SerializedName("errormessage") val errorMessage: String,
    @SerializedName("numberofresults") val numberOfResults: Int? = null,
    @SerializedName("timestamp") val timestamp: String? = null,
    @SerializedName("results") val results: List<RtpiBusStopInformationJson> = mutableListOf()
)

data class RtpiBusStopInformationJson(
    @SerializedName("stopid") val stopId: String? = null,
    @SerializedName("displaystopid") val displayId: String? = null,
    @SerializedName("shortname") val shortName: String? = null,
    @SerializedName("shortnamelocalized") val shortNameLocalized: String? = null,
    @SerializedName("fullname") val fullName: String? = null,
    @SerializedName("fullnamelocalized") val fullNameLocalized: String? = null,
    @SerializedName("latitude") val latitude: String? = null,
    @SerializedName("longitude") val longitude: String? = null,
    @SerializedName("lastupdated") val lastUpdated: String? = null,
    @SerializedName("operators") val operators: List<RtpiBusStopOperatorInformationJson> = mutableListOf()
)

data class RtpiBusStopOperatorInformationJson(
    @SerializedName("name") val name: String? = null,
    @SerializedName("operatortype") val operatorType: Int? = null,
    @SerializedName("routes") val routes: List<String> = mutableListOf()
)
