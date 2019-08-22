package com.mvp.accontrol.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mvp.accontrol.viewstate.ACUnitListViewState

class ACUnitListViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ACUnitListViewModel(ACUnitListViewState()) as T
    }
}