package com.mvp.accontrol.data

import com.google.gson.annotations.SerializedName

data class DeviceResponse(
    @SerializedName("success")
    val success: Boolean?
)