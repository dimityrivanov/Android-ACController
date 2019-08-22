package com.mvp.accontrol.data.service

import com.mvp.accontrol.data.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ACClimateService {
    companion object {
        private const val DEVICE_INIT = "init"
        private const val DEVICE_STATE_CHANGE = "params"
        private const val DEVICE_EMIT = "emit"
        private const val DEVICE_ID = "/index.html"
        private const val DEVICE_STATE_RESPONSE = "state"
    }

    @GET(DEVICE_ID)
    fun getDeviceID(): Call<DeviceIDResponse>

    @POST(DEVICE_INIT)
    fun initDevice(@Body initRequest: InitDeviceRequest): Call<DeviceResponse>

    @POST(DEVICE_STATE_CHANGE)
    fun setDeviceState(@Body stateRequest: DeviceStateRequest): Call<DeviceResponse>

    @GET(DEVICE_EMIT)
    fun deviceEmit(): Call<DeviceResponse>

    @GET(DEVICE_STATE_RESPONSE)
    fun getDeviceState(): Call<DeviceStateResponse>
}