package io.rtpi.ktx

import retrofit2.Call

fun <T> Call<T>.validate(): T {
    val response = execute()
    if (response.isSuccessful && response.body() != null) {
        return response.body()!!
    }
    TODO()
}
