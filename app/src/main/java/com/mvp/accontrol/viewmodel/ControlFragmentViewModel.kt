package com.mvp.accontrol.viewmodel

import androidx.lifecycle.MutableLiveData
import com.mvp.accontrol.ACUnitState
import com.mvp.accontrol.data.DeviceResponse
import com.mvp.accontrol.data.DeviceStateRequest
import com.mvp.accontrol.data.service.ACClimateService
import com.mvp.accontrol.helper.RetrofitRestClient
import com.mvp.accontrol.helper.SafeRequestExecutor.execute
import com.mvp.accontrol.viewstate.ControlFragmentViewState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class ControlFragmentViewModel(
    private val viewState: ControlFragmentViewState
) : BaseViewModel() {

    var acUnitState: ACUnitState? = null

    val mainViewState = MutableLiveData<ControlFragmentViewState>().apply {
        value = viewState
    }

    private val mClimateService: ACClimateService by lazy {
        RetrofitRestClient.createService(ACClimateService::class.java)
    }

    fun setDeviceState() {
        mUiScope.launch {
            val deviceResponse = async(Dispatchers.IO) {
                return@async execute(
                    mClimateService.setDeviceState(
                        DeviceStateRequest(
                            fan = acUnitState?.fanState?.stateValue(),
                            mode = acUnitState?.modeState?.stateValue(),
                            temp = acUnitState?.acTemp,
                            power = acUnitState?.powerState?.stateValue()
                        )
                    )
                )
            }.await() as? DeviceResponse

            mainViewState.value = viewState.copy(deviceResponse = deviceResponse)
        }
    }

    fun deviceEmit() {
        mUiScope.launch {
            val deviceResponse = async(Dispatchers.IO) {
                return@async execute(mClimateService.deviceEmit())
            }.await() as? DeviceResponse
            // mainViewState.value = viewState.copy(deviceResponse = deviceResponse)
        }
    }
}