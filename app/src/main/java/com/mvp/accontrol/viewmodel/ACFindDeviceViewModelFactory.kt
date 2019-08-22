package com.mvp.accontrol.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ACFindDeviceViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ACFindDeviceViewModel(ACFindDeviceViewState()) as T
    }
}