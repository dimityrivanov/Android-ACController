package com.mvp.accontrol.data

import com.google.gson.annotations.SerializedName

data class InitDeviceRequest(
    @SerializedName("ac_device")
    val acDevice: Int?
)