package com.mvp.accontrol.states

import android.content.Context
import com.mvp.accontrol.R

class CoolModeState : IState() {
    companion object {
        private const val HARDWARE_DEVICE_COOL_CONSTANT = 2
    }

    override fun stateValue(): Int = HARDWARE_DEVICE_COOL_CONSTANT

    override fun stateResult(context: Context): String = context?.getString(R.string.cool)

    override fun nextState(): IState = DryModeState()
}