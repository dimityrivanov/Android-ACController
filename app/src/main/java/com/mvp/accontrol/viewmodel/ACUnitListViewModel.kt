package com.mvp.accontrol.viewmodel

import androidx.lifecycle.MutableLiveData
import com.mvp.accontrol.data.DeviceResponse
import com.mvp.accontrol.data.InitDeviceRequest
import com.mvp.accontrol.data.service.ACClimateService
import com.mvp.accontrol.helper.RetrofitRestClient
import com.mvp.accontrol.helper.SafeRequestExecutor.execute
import com.mvp.accontrol.viewstate.ACUnitListViewState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class ACUnitListViewModel(
    private var viewState: ACUnitListViewState
) : BaseViewModel() {

    var deviceBrand = MutableLiveData<Int>()

    val mainViewState = MutableLiveData<ACUnitListViewState>().apply {
        value = viewState
    }

    private val mClimateService: ACClimateService by lazy {
        RetrofitRestClient.createService(ACClimateService::class.java)
    }

    fun initDevice(ac_device: Int) {
        mUiScope.launch {
            val deviceResponse = async(Dispatchers.IO) {
                return@async execute(mClimateService.initDevice(InitDeviceRequest(acDevice = ac_device)))
            }.await() as? DeviceResponse

            mainViewState.value = viewState.copy(deviceResponse = deviceResponse)
        }
    }

}