package com.mvp.accontrol

import com.google.gson.annotations.SerializedName
import com.mvp.accontrol.states.IState

data class ACUnitState(
    @SerializedName("temp")
    var acTemp: Int = 0,
    @SerializedName("fanModeValue")
    var modeState: IState? = null,
    @SerializedName("fan")
    var fanState: IState? = null,
    @SerializedName("power")
    var powerState: IState? = null
)