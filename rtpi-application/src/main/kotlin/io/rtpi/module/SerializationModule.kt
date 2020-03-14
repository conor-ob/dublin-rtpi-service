package io.rtpi.module

import com.authzee.kotlinguice4.KotlinModule
import com.google.inject.Provides
import com.google.inject.Singleton
import com.google.inject.name.Named
import retrofit2.Converter
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.simplexml.SimpleXmlConverterFactory

class SerializationModule : KotlinModule() {

    @Provides
    @Singleton
    @Named("json")
    fun jsonConverterFactory(): Converter.Factory = GsonConverterFactory.create()

    @Provides
    @Singleton
    @Named("xml")
    fun xmlConverterFactory(): Converter.Factory = SimpleXmlConverterFactory.create()
}
