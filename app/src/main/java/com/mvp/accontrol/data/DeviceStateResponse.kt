package com.mvp.accontrol.data


import com.google.gson.annotations.SerializedName

data class DeviceStateResponse(
    @SerializedName("checksum")
    val checksum: Int?,
    @SerializedName("fanSpeedValue")
    val fanSpeedValue: Int?,
    @SerializedName("fanModeValue")
    val fanModeValue: Int?,
    @SerializedName("powerStateValue")
    val powerStateValue: Int?,
    @SerializedName("temp")
    val temp: Int?
)