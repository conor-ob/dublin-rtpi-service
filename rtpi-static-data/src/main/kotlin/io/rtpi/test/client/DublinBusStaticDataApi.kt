package io.rtpi.test.client

import io.reactivex.Single
import io.rtpi.external.dublinbus.DublinBusApi
import io.rtpi.external.dublinbus.DublinBusDestinationRequestXml
import io.rtpi.external.dublinbus.DublinBusDestinationResponseXml
import io.rtpi.external.dublinbus.DublinBusRealTimeStopDataRequestXml
import io.rtpi.external.dublinbus.DublinBusRealTimeStopDataResponseXml
import org.simpleframework.xml.core.Persister

class DublinBusStaticDataApi : DublinBusApi {

    override fun getAllDestinations(
        body: DublinBusDestinationRequestXml
    ): Single<DublinBusDestinationResponseXml> =
        Single.just(
            Persister().read(
                DublinBusDestinationResponseXml::class.java,
                DublinBusStaticDataApi::class.java.getResource("/dublinbus/dublinbus/stops.xml").readText()
            )
        )

    override fun getRealTimeStopData(
        body: DublinBusRealTimeStopDataRequestXml
    ): Single<DublinBusRealTimeStopDataResponseXml> {
        TODO("Not yet implemented")
    }
}
