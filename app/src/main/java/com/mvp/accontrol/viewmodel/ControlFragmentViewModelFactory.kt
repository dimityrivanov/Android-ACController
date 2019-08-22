package com.mvp.accontrol.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mvp.accontrol.viewstate.ControlFragmentViewState

class ControlFragmentViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ControlFragmentViewModel(ControlFragmentViewState()) as T
    }
}