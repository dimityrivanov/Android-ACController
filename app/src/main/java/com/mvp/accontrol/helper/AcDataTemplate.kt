package com.mvp.accontrol.helper

import com.google.gson.annotations.SerializedName

data class AcDataTemplate(
    @SerializedName("deviceID")
    var deviceID: String? = null,
    @SerializedName("inited")
    var inited: Boolean = false,
    @SerializedName("fanModeValue")
    var ac_modeState: String? = null,
    @SerializedName("fan")
    var ac_fanState: String? = null,
    @SerializedName("power")
    var ac_powerState: String? = null,
    @SerializedName("temp")
    var ac_temp: Int = -1
)