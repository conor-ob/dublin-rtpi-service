package io.rtpi.service.buseireann

import io.rtpi.api.Operator
import io.rtpi.external.rtpi.RtpiApi
import io.rtpi.external.rtpi.RtpiBusStopOperatorInformationJson
import io.rtpi.external.rtpi.createRtpiBusStopInformationJson
import io.rtpi.external.rtpi.createRtpiBusStopOperatorInformationJson
import io.rtpi.service.rtpi.AbstractRtpiStopService
import io.rtpi.service.rtpi.AbstractRtpiStopServiceTest

class BusEireannStopServiceTest : AbstractRtpiStopServiceTest() {

    override fun getOperator() = Operator.BUS_EIREANN

    override fun createStopService(rtpiApi: RtpiApi) = BusEireannStopService(rtpiApi) as AbstractRtpiStopService<*>

    override fun createDefaultStop() = createRtpiBusStopInformationJson(
        stopId = "601401",
        fullName = "Tubberquack Road",
        latitude = "53.342254",
        longitude = "-6.223423",
        operators = listOf(
            createRtpiBusStopOperatorInformationJson(
                name = "BE",
                routes = listOf("100", "100X", "101")
            )
        )
    )
}
