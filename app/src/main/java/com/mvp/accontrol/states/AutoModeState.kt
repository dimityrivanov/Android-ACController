package com.mvp.accontrol.states

import android.content.Context
import com.mvp.accontrol.R

class AutoModeState : IState() {
    companion object {
        private const val HARDWARE_DEVICE_AUTO_CONSTANT = 1
    }

    override fun stateValue(): Int = HARDWARE_DEVICE_AUTO_CONSTANT

    override fun stateResult(context: Context): String = context?.getString(R.string.auto)

    override fun nextState(): IState = CoolModeState()
}