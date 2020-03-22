package io.rtpi.external.rtpi

fun createRtpiBusStopInformationResponseJson(
    errorCode: String? = null,
    errorMessage: String? = null,
    numberOfResults: Int? = null,
    timestamp: String? = null,
    results: List<RtpiBusStopInformationJson>? = null
) = RtpiBusStopInformationResponseJson(
    errorCode = errorCode,
    errorMessage = errorMessage,
    numberOfResults = numberOfResults,
    timestamp = timestamp,
    results = results
)

fun createRtpiBusStopInformationJson(
    stopId: String? = null,
    displayId: String? = null,
    shortName: String? = null,
    shortNameLocalized: String? = null,
    fullName: String? = null,
    fullNameLocalized: String? = null,
    latitude: String? = null,
    longitude: String? = null,
    lastUpdated: String? = null,
    operators: List<RtpiBusStopOperatorInformationJson>? = null
) = RtpiBusStopInformationJson(
    stopId = stopId,
    displayId = displayId,
    shortName = shortName,
    shortNameLocalized = shortNameLocalized,
    fullName = fullName,
    fullNameLocalized = fullNameLocalized,
    latitude = latitude,
    longitude = longitude,
    lastUpdated = lastUpdated,
    operators = operators
)

fun createRtpiBusStopOperatorInformationJson(
    name: String? = null,
    operatorType: Int? = null,
    routes: List<String>? = null
) = RtpiBusStopOperatorInformationJson(
    name = name,
    operatorType = operatorType,
    routes = routes
)
