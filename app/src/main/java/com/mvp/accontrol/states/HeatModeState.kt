package com.mvp.accontrol.states

import android.content.Context
import com.mvp.accontrol.R

class HeatModeState : IState() {
    companion object {
        private const val HARDWARE_DEVICE_HEAT_CONSTANT = 4
    }

    override fun stateValue(): Int = HARDWARE_DEVICE_HEAT_CONSTANT

    override fun stateResult(context: Context): String = context?.getString(R.string.heat)

    override fun nextState(): IState = AutoModeState()
}