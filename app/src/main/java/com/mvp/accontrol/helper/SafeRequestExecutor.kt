package com.mvp.accontrol.helper

import retrofit2.Call

object SafeRequestExecutor {

    fun <T> execute(call: Call<T>): Any? {
        return try {
            call.execute().body()
        } catch (ex: Exception) {
            null
        }
    }
}