package com.mvp.accontrol.viewmodel

import com.mvp.accontrol.data.DeviceIDResponse
import com.mvp.accontrol.data.DeviceStateResponse

data class ACFindDeviceViewState(
    var address: String? = null,
    var deviceID: DeviceIDResponse? = null,
    var deviceState: DeviceStateResponse? = null
)