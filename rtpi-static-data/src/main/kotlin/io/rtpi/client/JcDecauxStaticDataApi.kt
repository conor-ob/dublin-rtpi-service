package io.rtpi.client

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.reactivex.Single
import io.rtpi.external.jcdecaux.JcDecauxApi
import io.rtpi.external.jcdecaux.StationJson

class JcDecauxStaticDataApi(
    private val gson: Gson
) : JcDecauxApi {

    override fun stations(contract: String, apiKey: String): Single<List<StationJson>> =
        Single.just(
            gson.fromJson(
                JcDecauxStaticDataApi::class.java.getResource("/dublinbikes/docks.json").readText(),
                object : TypeToken<List<StationJson>>() {}.type
            )
        )

    override fun station(stationNumber: String, contract: String, apiKey: String): Single<StationJson> {
        TODO("Not yet implemented")
    }
}
