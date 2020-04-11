package io.rtpi.client

import okhttp3.OkHttpClient

data class RtpiClientConfiguration(
    val okHttpClient: OkHttpClient? = null,
    val dublinBikesApiKey: String
)
