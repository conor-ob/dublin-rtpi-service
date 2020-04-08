package io.rtpi.service.dublinbus

import io.rtpi.api.Operator
import io.rtpi.external.rtpi.RtpiApi
import io.rtpi.external.rtpi.createRtpiBusStopInformationJson
import io.rtpi.external.rtpi.createRtpiBusStopOperatorInformationJson
import io.rtpi.service.rtpi.RtpiStopServiceTest

class DublinBusStopServiceTest : RtpiStopServiceTest() {

    override fun getOperator() = Operator.DUBLIN_BUS

    override fun createStopService(rtpiApi: RtpiApi) = DublinBusRtpiInternalStopService(rtpiApi, getOperator().shortName)

    override fun createDefaultStop() = createRtpiBusStopInformationJson(
        stopId = "315",
        fullName = "Bachelor's Walk",
        latitude = "53.12332",
        longitude = "-6.54938",
        operators = listOf(
            createRtpiBusStopOperatorInformationJson(
                name = "BAC",
                routes = listOf("46A", "145", "39A", "15", "747", "115")
            )
        )
    )
}
