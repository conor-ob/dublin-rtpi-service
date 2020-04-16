package io.rtpi.client

import com.google.gson.Gson
import io.reactivex.Single
import io.rtpi.api.Operator
import io.rtpi.external.rtpi.RtpiApi
import io.rtpi.external.rtpi.RtpiBusStopInformationResponseJson
import io.rtpi.external.rtpi.RtpiRealTimeBusInformationResponseJson

class RtpiStaticDataApi(
    private val gson: Gson
) : RtpiApi {

    override fun busStopInformation(
        operator: String,
        format: String
    ): Single<RtpiBusStopInformationResponseJson> =
        when (operator.toUpperCase()) {
            Operator.BUS_EIREANN.shortName -> {
                Single.just(
                    gson.fromJson(
                        RtpiStaticDataApi::class.java.getResource("/buseireann/stops.json").readText(),
                        RtpiBusStopInformationResponseJson::class.java
                    )
                )
            }
            Operator.LUAS.shortName -> {
                Single.just(
                    gson.fromJson(
                        RtpiStaticDataApi::class.java.getResource("/luas/stops.json").readText(),
                        RtpiBusStopInformationResponseJson::class.java
                    )
                )
            }
            Operator.DUBLIN_BUS.shortName -> {
                Single.just(
                    gson.fromJson(
                        RtpiStaticDataApi::class.java.getResource("/dublinbus/dublinbus/stops.json").readText(),
                        RtpiBusStopInformationResponseJson::class.java
                    )
                )
            }
            Operator.GO_AHEAD.shortName -> {
                Single.just(
                    gson.fromJson(
                        RtpiStaticDataApi::class.java.getResource("/dublinbus/goahead/stops.json").readText(),
                        RtpiBusStopInformationResponseJson::class.java
                    )
                )
            }
            else -> TODO()
        }

    override fun realTimeBusInformation(
        stopId: String,
        operator: String,
        format: String
    ): Single<RtpiRealTimeBusInformationResponseJson> {
        TODO("Not yet implemented")
    }
}
