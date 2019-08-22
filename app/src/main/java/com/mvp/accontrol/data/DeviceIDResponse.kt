package com.mvp.accontrol.data

import com.google.gson.annotations.SerializedName

data class DeviceIDResponse(
    @SerializedName("deviceID")
    val deviceID: String?
)