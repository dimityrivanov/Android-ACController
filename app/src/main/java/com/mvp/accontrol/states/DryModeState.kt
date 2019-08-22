package com.mvp.accontrol.states

import android.content.Context
import com.mvp.accontrol.R

class DryModeState : IState() {
    companion object {
        private const val HARDWARE_DEVICE_DRY_CONSTANT = 3
    }

    override fun stateValue(): Int = HARDWARE_DEVICE_DRY_CONSTANT

    override fun stateResult(context: Context): String = context?.getString(R.string.dry)

    override fun nextState(): IState = HeatModeState()
}