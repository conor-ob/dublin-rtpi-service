package io.rtpi.external.rtpi

import com.google.gson.annotations.SerializedName

data class RtpiBusStopInformationResponseJson(
    @SerializedName("errorcode") var errorCode: String? = null,
    @SerializedName("errormessage") var errorMessage: String? = null,
    @SerializedName("numberofresults") var numberOfResults: Int? = null,
    @SerializedName("timestamp") var timestamp: String? = null,
    @SerializedName("results") var results: List<RtpiBusStopInformationJson> = emptyList()
)

data class RtpiBusStopInformationJson(
    @SerializedName("stopid") var stopId: String? = null,
    @SerializedName("displaystopid") var displayId: String? = null,
    @SerializedName("shortname") var shortName: String? = null,
    @SerializedName("shortnamelocalized") var shortNameLocalized: String? = null,
    @SerializedName("fullname") var fullName: String? = null,
    @SerializedName("fullnamelocalized") var fullNameLocalized: String? = null,
    @SerializedName("latitude") var latitude: String? = null,
    @SerializedName("longitude") var longitude: String? = null,
    @SerializedName("lastupdated") var lastUpdated: String? = null,
    @SerializedName("operators") var operators: List<RtpiBusStopOperatorInformationJson> = emptyList()
)

data class RtpiBusStopOperatorInformationJson(
    @SerializedName("name") var name: String? = null,
    @SerializedName("operatortype") var operatorType: Int? = null,
    @SerializedName("routes") var routes: List<String> = emptyList()
)
