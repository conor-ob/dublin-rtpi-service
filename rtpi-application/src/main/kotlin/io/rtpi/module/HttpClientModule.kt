package io.rtpi.module

import com.authzee.kotlinguice4.KotlinModule
import com.google.inject.Provides
import com.google.inject.Singleton
import com.google.inject.name.Named
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.X509TrustManager
import okhttp3.OkHttpClient

class HttpClientModule : KotlinModule() {

    @Provides
    @Singleton
    @Named("default_client")
    fun httpClient(): OkHttpClient = OkHttpClient.Builder()
        .retryOnConnectionFailure(true)
        .connectTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .build()

    @Provides
    @Singleton
    @Named("aircoach_client")
    fun aircoachHttpClient(): OkHttpClient {
        val sslContext = SSLContext.getInstance("TLS")
        sslContext.init(
            null,
            arrayOf<X509TrustManager>(object : X509TrustManager {
                override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {
                }

                override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {
                }

                override fun getAcceptedIssuers(): Array<X509Certificate> {
                    return emptyArray()
                }
            }),
            SecureRandom()
        )
        return OkHttpClient.Builder()
            .hostnameVerifier { hostname, session ->
                return@hostnameVerifier hostname == "tracker.aircoach.ie" &&
                    session.peerHost == "tracker.aircoach.ie" &&
                    session.peerPort == 443
            }
            .sslSocketFactory(sslContext.socketFactory)
            .retryOnConnectionFailure(true)
            .connectTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .build()
    }
}
