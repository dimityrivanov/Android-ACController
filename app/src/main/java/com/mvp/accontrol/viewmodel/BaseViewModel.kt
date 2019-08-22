package com.mvp.accontrol.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

abstract class BaseViewModel : ViewModel() {
    private val mJob = Job()

    protected val mUiScope = CoroutineScope(Dispatchers.Main + mJob)
    protected val mHandler = CoroutineExceptionHandler { _, throwable ->
        //AppLogger.e(throwable)
    }

    override fun onCleared() {
        super.onCleared()

        mJob.cancel()
    }
}