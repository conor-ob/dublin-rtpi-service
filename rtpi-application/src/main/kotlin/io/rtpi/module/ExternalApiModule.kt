package io.rtpi.module

import com.authzee.kotlinguice4.KotlinModule
import com.google.inject.Provides
import com.google.inject.Singleton
import com.google.inject.name.Named
import io.rtpi.RtpiServiceConfiguration
import io.rtpi.external.aircoach.AircoachApi
import io.rtpi.external.aircoach.AircoachWebScraper
import io.rtpi.external.dublinbus.DublinBusApi
import io.rtpi.external.irishrail.IrishRailApi
import io.rtpi.external.jcdecaux.JcDecauxApi
import io.rtpi.external.rtpi.RtpiApi
import io.rtpi.external.staticdata.StaticDataApi
import io.rtpi.service.aircoach.JavascriptEngineAircoachWebScraper
import okhttp3.OkHttpClient
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit

class ExternalApiModule : KotlinModule() {

    @Provides
    @Singleton
    fun aircoachWebScraper(
        configuration: RtpiServiceConfiguration
    ): AircoachWebScraper = JavascriptEngineAircoachWebScraper(requireNotNull(configuration.apiConfiguration.aircoachBaseUrl))

    @Provides
    @Singleton
    fun aircoachApi(
        configuration: RtpiServiceConfiguration,
        @Named("aircoach_client") client: OkHttpClient,
        callAdapterFactory: CallAdapter.Factory,
        @Named("json") converterFactory: Converter.Factory
    ): AircoachApi = Retrofit.Builder()
        .baseUrl(requireNotNull(configuration.apiConfiguration.aircoachBaseUrl))
        .client(client)
        .addCallAdapterFactory(callAdapterFactory)
        .addConverterFactory(converterFactory)
        .build()
        .create(AircoachApi::class.java)

    @Provides
    @Singleton
    fun dublinBusApi(
        configuration: RtpiServiceConfiguration,
        @Named("default_client") client: OkHttpClient,
        callAdapterFactory: CallAdapter.Factory,
        @Named("xml") converterFactory: Converter.Factory
    ): DublinBusApi = Retrofit.Builder()
        .baseUrl(requireNotNull(configuration.apiConfiguration.dublinBusBaseUrl))
        .client(client)
        .addCallAdapterFactory(callAdapterFactory)
        .addConverterFactory(converterFactory)
        .build()
        .create(DublinBusApi::class.java)

    @Provides
    @Singleton
    fun jcDecauxApi(
        configuration: RtpiServiceConfiguration,
        @Named("default_client") client: OkHttpClient,
        callAdapterFactory: CallAdapter.Factory,
        @Named("json") converterFactory: Converter.Factory
    ): JcDecauxApi = Retrofit.Builder()
        .baseUrl(requireNotNull(configuration.apiConfiguration.jcDecauxBaseUrl))
        .client(client)
        .addCallAdapterFactory(callAdapterFactory)
        .addConverterFactory(converterFactory)
        .build()
        .create(JcDecauxApi::class.java)

    @Provides
    @Singleton
    fun rtpiApi(
        configuration: RtpiServiceConfiguration,
        @Named("default_client") client: OkHttpClient,
        callAdapterFactory: CallAdapter.Factory,
        @Named("json") converterFactory: Converter.Factory
    ): RtpiApi = Retrofit.Builder()
        .baseUrl(requireNotNull(configuration.apiConfiguration.rtpiBaseUrl))
        .client(client)
        .addCallAdapterFactory(callAdapterFactory)
        .addConverterFactory(converterFactory)
        .build()
        .create(RtpiApi::class.java)

    @Provides
    @Singleton
    fun irishRailApi(
        configuration: RtpiServiceConfiguration,
        @Named("default_client") client: OkHttpClient,
        callAdapterFactory: CallAdapter.Factory,
        @Named("xml") converterFactory: Converter.Factory
    ): IrishRailApi = Retrofit.Builder()
        .baseUrl(requireNotNull(configuration.apiConfiguration.irishRailBaseUrl))
        .client(client)
        .addCallAdapterFactory(callAdapterFactory)
        .addConverterFactory(converterFactory)
        .build()
        .create(IrishRailApi::class.java)

    @Provides
    @Singleton
    fun staticDataApi(
        configuration: RtpiServiceConfiguration,
        @Named("default_client") client: OkHttpClient,
        callAdapterFactory: CallAdapter.Factory,
        @Named("json") jsonConverterFactory: Converter.Factory,
        @Named("xml") xmlConverterFactory: Converter.Factory
    ): StaticDataApi = Retrofit.Builder()
        .baseUrl("https://raw.githubusercontent.com/conor-ob/rtpi-static-data/master/")
        .addCallAdapterFactory(callAdapterFactory)
        .addConverterFactory(jsonConverterFactory)
        .addConverterFactory(xmlConverterFactory)
        .build()
        .create(StaticDataApi::class.java)
}
