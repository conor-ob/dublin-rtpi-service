package io.rtpi.external.aircoach

import com.google.gson.annotations.SerializedName

data class ServiceResponseJson(
    @SerializedName("services") var services: List<ServiceJson> = emptyList()
)

data class ServiceJson(
    @SerializedName("date") var date: String? = null,
    @SerializedName("linkDate") var linkDate: String? = null,
    @SerializedName("time") var time: TimeJson? = null,
    @SerializedName("startTime") var startTime: TimestampJson? = null,
    @SerializedName("route") var route: String? = null,
    @SerializedName("dir") var dir: String? = null,
    @SerializedName("colour") var colour: String? = null,
    @SerializedName("depart") var depart: String? = null,
    @SerializedName("arrivar") var arrival: String? = null,
    @SerializedName("journeyId") var journeyId: String? = null,
    @SerializedName("linkName") var linkName: String? = null,
    @SerializedName("stopType") var stopType: Int? = null,
    @SerializedName("stops") var stops: List<String> = emptyList(),
    @SerializedName("places") var places: List<String> = emptyList(),
    @SerializedName("live") var live: LiveJson? = null,
    @SerializedName("dups") var dups: String? = null,
    @SerializedName("eta") var eta: EtaJson? = null,
    @SerializedName("delayed") var delayed: Boolean? = null
)

data class EtaJson(
    @SerializedName("etaArrive") var etaArrive: TimestampJson? = null,
    @SerializedName("etaDepart") var etaDepart: TimestampJson? = null,
    @SerializedName("etaLayover") var etaLayover: Int? = null,
    @SerializedName("arrive") var arrive: TimestampJson? = null,
    @SerializedName("depart") var depart: TimestampJson? = null,
    @SerializedName("atStop") var atStop: Boolean? = null,
    @SerializedName("late") var late: Boolean? = null,
    @SerializedName("status") var status: String? = null,
    @SerializedName("timestamp") var timestamp: String? = null
)

data class LiveJson(
    @SerializedName("vehicleId") var vehicleId: String? = null,
    @SerializedName("vehicle") var vehicle: String? = null,
    @SerializedName("lat") var lat: Double? = null,
    @SerializedName("lon") var lon: Double? = null,
    @SerializedName("bearing") var bearing: Int? = null,
    @SerializedName("status") var status: String? = null,
    @SerializedName("timestamp") var timestamp: TimestampJson? = null,
    @SerializedName("timeZone") var timeZone: String? = null,
    @SerializedName("geoLocation") var geoLocation: String? = null,
    @SerializedName("gpsProvider") var gpsProvider: String? = null
)

data class TimeJson(
    @SerializedName("arrive") var arrive: TimestampJson? = null,
    @SerializedName("depart") var depart: TimestampJson? = null,
    @SerializedName("layover") var layover: Int? = null,
    @SerializedName("status") var status: Int? = null,
    @SerializedName("timeZone") var timeZone: String? = null,
    @SerializedName("actualStatus") var actualStatus: Int? = null,
    @SerializedName("duplicateCount") var duplicateCount: Int? = null
)

data class TimestampJson(
    @SerializedName("dateTime") var dateTime: String? = null,
    @SerializedName("hrs") var hrs: Int? = null,
    @SerializedName("mins") var mins: Int? = null
)
