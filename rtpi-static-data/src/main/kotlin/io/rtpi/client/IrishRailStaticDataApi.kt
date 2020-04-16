package io.rtpi.client

import io.reactivex.Single
import io.rtpi.external.irishrail.IrishRailApi
import io.rtpi.external.irishrail.IrishRailStationDataResponseXml
import io.rtpi.external.irishrail.IrishRailStationResponseXml
import org.simpleframework.xml.core.Persister

class IrishRailStaticDataApi : IrishRailApi {

    override fun getAllStationsXml(): Single<IrishRailStationResponseXml> =
        Single.just(
            Persister().read(
                IrishRailStationResponseXml::class.java,
                IrishRailStaticDataApi::class.java.getResource("/irishrail/stations.xml").readText()
            )
        )

    override fun getStationDataByCodeXml(stationCode: String): Single<IrishRailStationDataResponseXml> {
        TODO("Not yet implemented")
    }
}
