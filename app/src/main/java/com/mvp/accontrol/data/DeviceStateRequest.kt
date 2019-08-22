package com.mvp.accontrol.data

import com.google.gson.annotations.SerializedName

data class DeviceStateRequest(
    @SerializedName("fan")
    val fan: Int?,
    @SerializedName("mode")
    val mode: Int?,
    @SerializedName("temp")
    val temp: Int?,
    @SerializedName("power")
    val power: Int?
)